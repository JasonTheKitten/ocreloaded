package li.cil.ocreloaded.core.network.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import li.cil.ocreloaded.core.network.Network;
import li.cil.ocreloaded.core.network.NetworkNode;
import li.cil.ocreloaded.core.network.NetworkNode.Visibility;

public class NetworkImp implements Network {

    private final Map<UUID, Set<NetworkNode>> connections = new HashMap<>();

    public NetworkImp(NetworkNode firstNode) {
        addNewNode(firstNode);
    }

    @Override
    public void connect(NetworkNode nodeA, NetworkNode nodeB) {
        boolean nodeAExists = connections.containsKey(nodeA.id());
        boolean nodeBExists = connections.containsKey(nodeB.id());

        if (nodeA == nodeB) throw new IllegalArgumentException("Cannot connect a node to itself.");
        if (!nodeAExists && !nodeBExists) throw new IllegalArgumentException("At least one of the nodes must already be in this network.");
        
        if (nodeAExists && nodeBExists) {
            if (!connections.get(nodeA.id()).contains(nodeB)) {
                assert !connections.get(nodeB.id()).contains(nodeA);
                connections.get(nodeA.id()).add(nodeB);
                connections.get(nodeB.id()).add(nodeA);

                if (nodeA.visibility() == Visibility.NEIGHBORS) nodeB.onConnect(nodeA);
                if (nodeB.visibility() == Visibility.NEIGHBORS) nodeA.onConnect(nodeB);
            }
        } else if (nodeAExists) {
            addNode(nodeA, nodeB);
        } else {
            addNode(nodeB, nodeA);
        }
    }

    @Override
    public void disconnect(NetworkNode nodeA, NetworkNode nodeB) {
        if (nodeA == nodeB) throw new IllegalArgumentException("Cannot disconnect a node from itself.");
        if (!connections.containsKey(nodeA.id())) throw new IllegalArgumentException("Node A is not in this network.");
        if (!connections.containsKey(nodeB.id())) throw new IllegalArgumentException("Node B is not in this network.");

        Set<NetworkNode> nodeAConnections = connections.get(nodeA.id());
        Set<NetworkNode> nodeBConnections = connections.get(nodeB.id());

        if (nodeAConnections.remove(nodeB) && nodeBConnections.remove(nodeA)) {
            handleSplit();
            if (nodeA.visibility() == Visibility.NEIGHBORS) {
                nodeB.onDisconnect(nodeA);
            }
            if (nodeB.visibility() == Visibility.NEIGHBORS) {
                nodeA.onDisconnect(nodeB);
            }
        }
    }

    @Override
    public void remove(NetworkNode node) {
        if (!connections.containsKey(node.id())) throw new IllegalArgumentException("Node is not in this network.");
        
        connections.remove(node.id());
        handleSplit();
    }

    @Override
    public void rename(UUID oldName, UUID newName) {
        if (!connections.containsKey(oldName)) throw new IllegalArgumentException("Node is not in this network.");

        connections.put(newName, connections.remove(oldName));
    }

    @Override
    public boolean reachable(NetworkNode source, NetworkNode target) {
        if (source == target) return false;
        if (source.network() != this) return false;
        if (target.network() != this) return false;
        return switch(target.visibility()) {
            case NONE -> false;
            case NEIGHBORS -> neighbors(source, target);
            case NETWORK -> true;
        };
    }

    @Override
    public Optional<NetworkNode> reachableNode(NetworkNode networkNode, UUID nodeId) {
        NetworkNode target = connections.getOrDefault(nodeId, Set.of()).stream()
            .filter(node -> node.id().equals(nodeId)).findFirst().orElse(null);
        return Optional.ofNullable(target).filter(targetNode -> reachable(networkNode, targetNode));
    }

    @Override
    public Set<NetworkNode> reachableNodes(NetworkNode node) {
        return allNodes().stream().filter(target -> reachable(node, target)).collect(Collectors.toSet());
    }

    private void addNewNode(NetworkNode node) {
        connections.computeIfAbsent(node.id(), id -> new HashSet<>()).add(node);
    }

    private void addNode(NetworkNode reference, NetworkNode node) {
        if (!(node.network() instanceof NetworkImp)) {
            throw new UnsupportedOperationException("Can currently only merge similar network types.");
        }

        NetworkImp otherNetwork = (NetworkImp) node.network();
        Set<NetworkNode> currentNetworkNodes = allNodes();
        Set<NetworkNode> otherNetworkNodes = otherNetwork.allNodes();

        for (NetworkNode nodeA : currentNetworkNodes) {
            for (NetworkNode nodeB : otherNetworkNodes) {
                if (nodeA.visibility() == Visibility.NETWORK) nodeB.onConnect(nodeA);
                if (nodeB.visibility() == Visibility.NETWORK) nodeA.onConnect(nodeB);
            }
        }

        if (reference.visibility() == Visibility.NEIGHBORS) node.onConnect(reference);
        if (node.visibility() == Visibility.NEIGHBORS) reference.onConnect(node);

        for (NetworkNode otherNode : otherNetworkNodes) {
            otherNode.onNetworkChange(otherNetwork, this);
        }

        connections.putAll(otherNetwork.connections);
        connections.get(reference.id()).add(node);
        connections.get(node.id()).add(reference);
        otherNetwork.connections.clear();
    }

    private Set<NetworkNode> allNodes() {
        return connections.values().stream().flatMap(Set::stream).collect(Collectors.toSet());
    }

    private boolean neighbors(NetworkNode nodeA, NetworkNode nodeB) {
        return connections.getOrDefault(nodeA.id(), Set.of()).contains(nodeB);
    }

    private void handleSplit() {
        List<Set<NetworkNode>> subGraphs = new ArrayList<>();
        Set<NetworkNode> visited = new HashSet<>();

        for (NetworkNode node : allNodes()) {
            if (!visited.contains(node)) {
                Set<NetworkNode> group = new HashSet<>();
                exploreGroup(node, group, visited);
                subGraphs.add(group);
            }
        }

        if (subGraphs.size() > 1) {
            createSubgraphNetworks(subGraphs);
        }
    }

    private void createSubgraphNetworks(List<Set<NetworkNode>> subGraphs) {
        List<Set<NetworkNode>> visibleNodes = subGraphs.stream()
            .map(group -> group.stream()
                .filter(n -> n.visibility() == Visibility.NETWORK)
                .collect(Collectors.toSet()))
            .toList();

        for (Set<NetworkNode> subgraph : subGraphs) {
            NetworkImp newNetwork = new NetworkImp(subgraph.iterator().next());
            subgraph.forEach(newNetwork::addNewNode);
        };

        for (int i = 0; i < subGraphs.size(); i++) {
            Set<NetworkNode> nodesA = subGraphs.get(i);
            Set<NetworkNode> visibleA = visibleNodes.get(i);
            for (int j = i + 1; j < subGraphs.size(); j++) {
                Set<NetworkNode> nodesB = subGraphs.get(j);
                Set<NetworkNode> visibleB = visibleNodes.get(j);
                visibleA.forEach(node -> nodesB.forEach(n -> n.onDisconnect(node)));
                visibleB.forEach(node -> nodesA.forEach(n -> n.onDisconnect(node)));
            }
        }
    }

    private void exploreGroup(NetworkNode node, Set<NetworkNode> group, Set<NetworkNode> visited) {
        if (visited.add(node)) {
            group.add(node);
            connections.getOrDefault(node.id(), Set.of()).forEach(neighbor -> exploreGroup(neighbor, group, visited));
        }
    }
    
}
