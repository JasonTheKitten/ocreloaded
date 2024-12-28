package li.cil.ocreloaded.core.network.imp;

import java.util.HashMap;
import java.util.HashSet;
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

        // TODO
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
    
}
