package li.cil.ocreloaded.generator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class BlockstateJsonGenerator {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static void main(String[] args) {
        try {
            generateBlockStates(args[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateBlockStates(String outputDir) throws IOException {
        Files.createDirectories(Paths.get(outputDir, "blockstates"));
        Files.createDirectories(Paths.get(outputDir, "models/block/screen"));

        Map<String, Object> variants = new HashMap<>();
        generateModelsAndVariants(outputDir, "north", variants);
        generateBlockstateFiles(outputDir, variants);
    }

    private static void generateModelsAndVariants(String outputDir, String baseFacing, Map<String, Object> variants) throws IOException {
        for (String face : new String[]{ "wall", "floor", "ceiling" }) {
            for (boolean up : new boolean[]{ true, false }) {
                for (boolean down : new boolean[]{ true, false }) {
                    for (boolean left : new boolean[]{ true, false }) {
                        for (boolean right : new boolean[]{ true, false }) {
                            String modelName = generateModelName(baseFacing, face, up, down, left, right);
                            generateModelFile(outputDir, modelName, baseFacing, face, up, down, left, right);
                            for (String facing : new String[]{ "north", "south", "east", "west" }) {
                                String variantKey = generateVariantKey(facing, face, up, down, left, right);
                                variants.put(variantKey, generateBlockstateVariant(modelName, facing, face));
                            }
                        }
                    }
                }
            }
        }
    }

    private static String generateVariantKey(String facing, String face, boolean up, boolean down, boolean left, boolean right) {
        return String.format("facing=%s,face=%s,up=%b,down=%b,left=%b,right=%b", facing, face, up, down, left, right);
    }

    private static String generateModelName(String facing, String face, boolean up, boolean down, boolean left, boolean right) {
        StringBuilder nameBuilder = new StringBuilder("screen_");
        nameBuilder.append(face);

        if (up) nameBuilder.append("_up");
        if (down) nameBuilder.append("_down");
        if (left) nameBuilder.append("_left");
        if (right) nameBuilder.append("_right");

        return nameBuilder.toString();
    }

    private static Map<String, Object> generateBlockstateVariant(String modelName, String facing, String face) {
        Map<String, Object> variant = new HashMap<>();
        variant.put("model", "ocreloaded:block/screen/" + modelName);

        switch (facing) {
            case "north" -> variant.put("y", 0);
            case "south" -> variant.put("y", 180);
            case "east" -> variant.put("y", 90);
            case "west" -> variant.put("y", 270);
        }

        return variant;
    }

    private static void generateModelFile(String outputDir, String modelName, String baseFacing, String face, boolean up, boolean down, boolean left, boolean right) throws IOException {
        Map<String, Object> modelJson = new HashMap<>();
        modelJson.put("parent", "block/cube");

        Map<String, String> textures = new HashMap<>();
        switch (face) {
            case "floor" -> {
                textures.put("down", "ocreloaded:" + determineTextureName(baseFacing, face, Side.BACK, up, down, left, right));
                textures.put("up", "ocreloaded:" + determineTextureName(baseFacing, face, Side.FRONT, up, down, left, right));
                textures.put("north", "ocreloaded:" + determineTextureName(baseFacing, face, Side.TOP, up, down, left, right));
                textures.put("south", "ocreloaded:" + determineTextureName(baseFacing, face, Side.BOTTOM, up, down, left, right));
                textures.put("east", "ocreloaded:" + determineTextureName(baseFacing, face, Side.RIGHT, up, down, left, right));
                textures.put("west", "ocreloaded:" + determineTextureName(baseFacing, face, Side.LEFT, up, down, left, right));
            }
            case "ceiling" -> {
                textures.put("down", "ocreloaded:" + determineTextureName(baseFacing, face, Side.FRONT, up, down, left, right));
                textures.put("up", "ocreloaded:" + determineTextureName(baseFacing, face, Side.BACK, up, down, left, right));
                textures.put("north", "ocreloaded:" + determineTextureName(baseFacing, face, Side.TOP, up, down, left, right));
                textures.put("south", "ocreloaded:" + determineTextureName(baseFacing, face, Side.BOTTOM, up, down, left, right));
                textures.put("east", "ocreloaded:" + determineTextureName(baseFacing, face, Side.RIGHT, up, down, left, right));
                textures.put("west", "ocreloaded:" + determineTextureName(baseFacing, face, Side.LEFT, up, down, left, right));
            }
            default -> {
                textures.put("north", "ocreloaded:" + determineTextureName(baseFacing, face, Side.FRONT, up, down, left, right));
                textures.put("south", "ocreloaded:" + determineTextureName(baseFacing, face, Side.BACK, up, down, left, right));
                textures.put("east", "ocreloaded:" + determineTextureName(baseFacing, face, Side.RIGHT, up, down, left, right));
                textures.put("west", "ocreloaded:" + determineTextureName(baseFacing, face, Side.LEFT, up, down, left, right));
                textures.put("up", "ocreloaded:" + determineTextureName(baseFacing, face, Side.TOP, up, down, left, right));
                textures.put("down", "ocreloaded:" + determineTextureName(baseFacing, face, Side.BOTTOM, up, down, left, right));
            }
        }
        modelJson.put("textures", textures);

        Map<String, Object> faces = new HashMap<>();
        for (String faceName : new String[]{ "north", "south", "east", "west", "up", "down" }) {
            Map<String, Object> faceJson = new HashMap<>();
            faceJson.put("texture", "#" + faceName);
            faceJson.put("cullface", faceName);
            faceJson.put("tintindex", 0);
            faces.put(faceName, faceJson);
        }

        Map<String, Object> elements = new HashMap<>();
        elements.put("from", new double[]{ 0, 0, 0 });
        elements.put("to", new double[]{ 16, 16, 16 });
        elements.put("faces", faces);

        modelJson.put("elements", new Object[] { elements });

        writeJsonToFile(Paths.get(outputDir, "models/block/screen", modelName + ".json"), modelJson);
    }

    private static String determineTextureName(String facing, String face, Side side, boolean up, boolean down, boolean left, boolean right) {
        boolean isSideNonWall = !face.equals("wall") && (side != Side.FRONT && side != Side.BACK);
        
        boolean invertY = (face.equals("floor") && side == Side.FRONT) || (face.equals("ceiling") && side == Side.BACK);
        boolean invertedUp = invertY ? down : up;
        boolean invertedDown = invertY ? up : down;
        String upDown = isSideNonWall ? "single" : switch (side) {
            case FRONT, BACK, LEFT, RIGHT -> selectPart(invertedUp, invertedDown, "single", "bottom", "top", "middle");
            case TOP, BOTTOM -> "single";
        };

        String leftRight = !face.equals("wall") ?
            switch (side) {
                case BOTTOM -> selectPart(left, right, "single", "left", "right", "middle");
                case TOP -> selectPart(left, right, "single", "right", "left", "middle");
                case LEFT -> selectPart(up, down, "single", "left", "right", "middle");
                case RIGHT -> selectPart(up, down, "single", "right", "left", "middle");
                case FRONT, BACK -> selectPart(left, right, "single", "left", "right", "middle");
            } :
            switch (side) {
                case FRONT -> selectPart(left, right, "single", "right", "left", "middle");
                case BACK, TOP, BOTTOM -> selectPart(left, right, "single", "left", "right", "middle");
                case LEFT, RIGHT -> "single";
            };

        StringBuilder textureName = new StringBuilder("block/screen/");
        textureName.append(side == Side.FRONT ? "front" : "back");
        textureName.append("_").append(upDown).append("_").append(leftRight);

        if (
            ((face.equals("floor") || face.equals("ceiling")) && isSideNonWall)
            || (face.equals("wall") && !(side == Side.TOP || side == Side.BOTTOM) && !down)
        ) {
            textureName.append("_side");
        }

        return textureName.toString();
    }

    private static String selectPart(boolean a, boolean b, String nn, String an, String nb, String ab) {
        if (a && b) return ab;
        else if (a) return an;
        else if (b) return nb;
        else return nn;
    }

    private static void writeJsonToFile(Path path, Map<String, Object> json) throws IOException {
        try (FileWriter writer = new FileWriter(path.toFile())) {
            GSON.toJson(json, writer);
        }
    }

    private static void generateBlockstateFiles(String outputDir, Map<String, Object> variants) throws IOException {
        Map<String, Object> blockstateJson = new HashMap<>();
        blockstateJson.put("variants", variants);

        for (String fileName : new String[]{ "screen1.json", "screen2.json", "screen3.json" }) {
            writeJsonToFile(Paths.get(outputDir, "blockstates", fileName), blockstateJson);
        }
    }

    private enum Side {
        FRONT, BACK, LEFT, RIGHT, TOP, BOTTOM;
    }

}
