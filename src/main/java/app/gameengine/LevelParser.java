package app.gameengine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import app.display.common.Background;
import app.gameengine.model.gameobjects.DynamicGameObject;
import app.gameengine.model.gameobjects.StaticGameObject;
import app.games.commonobjects.*;
import app.games.mario.*;
import app.games.roguelikeobjects.DirectionalWall;
import app.games.roguelikeobjects.Marker;
import app.games.roguelikeobjects.RoguelikeLevel;
import app.games.topdownobjects.*;

/**
 * Static class for creating levels from formatted csv files.
 */
public class LevelParser {

    /**
     * Parse the csv file at the given location within the levels directory, and
     * return the level which that file represents.
     * 
     * @param game the game the level will be part of
     * @param path the path within the levels directory to the level
     * @return the parsed level
     */
    public static Level parseLevel(Game game, String path) {
        try {
            ArrayList<String> csvFile = new ArrayList<>(Files.readAllLines(Paths.get("./data/levels/" + path)));
            Level level = null;
            if (csvFile.isEmpty()) { return level; }

            for (int i=0; i < csvFile.size(); i++) {
                ArrayList<String> lines = new ArrayList<>(Arrays.asList(csvFile.get(i).split(",")));
                if (lines.isEmpty()) { continue; }
                if(i == 0) {
                    if (lines.getFirst().equals("TopDownLevel")) {
                        level = new TopDownLevel(game, Integer.parseInt(lines.get(2)), Integer.parseInt(lines.get(3)), lines.get(1));
                    }
                    else if (lines.getFirst().equals("MarioLevel")) {
                        level = new MarioLevel(game, Integer.parseInt(lines.get(2)), Integer.parseInt(lines.get(3)), lines.get(1));
                    }
                    else if (lines.getFirst().equals("RoguelikeLevel")) {
                        level = new RoguelikeLevel(game, Integer.parseInt(lines.get(2)), Integer.parseInt(lines.get(3)), lines.get(1));
                    }
                } else if (i == 1 && level != null) {
                    level.setPlayerStartLocation(Double.parseDouble(lines.get(1)), Double.parseDouble(lines.get(2)));
                }
                else if (i == 2 && level != null){
                    level.setBackground(readBackground(lines));
                } else if (level != null) {
                    if (lines.getFirst().equals("StaticGameObject")) {
                        StaticGameObject staticGameObject = readStaticObject(game, level, lines);
                        if (staticGameObject != null) { level.addStaticObject(staticGameObject);}
                    }
                    else if (lines.getFirst().equals("DynamicGameObject")) {
                        DynamicGameObject dynamicGameObject = readDynamicObject(game, level, lines);
                        if (dynamicGameObject != null) { level.addDynamicObject(dynamicGameObject);}
                    }
                }
            }
            return  level;
        }
        catch (IOException e) {
            return null;
        }
    }

    /**
     * Creates and returns a {@code DynamicGameObject} as defined by the input list
     * of properties. The format of the input {@code ArrayList} is:
     * <p>
     * "DynamicGameObject,SubType,x,y,..."
     * <p>
     * Where SubType is the name of the class, x and y are the location of the
     * object, and any following items are additional constructor parameters.
     * 
     * @param game  the game this object will be a member of
     * @param level the level this object will be a member of
     * @param split the split line from the csv file describing the object
     * @return the object that is described by {@code split}
     */
    public static DynamicGameObject readDynamicObject(Game game, Level level, ArrayList<String> split) {
        double x = Double.parseDouble(split.get(2));
        double y = Double.parseDouble(split.get(3));
        String type = split.get(1);
        if (type.equals("Demon")) {
            return new Demon(x, y, Integer.parseInt(split.get(4)), Integer.parseInt(split.get(5)));
        } else if (type.equals("Tower")) {
            return new Tower(x, y);
            
        } else if (type.equals("Goomba")) {
            return new Goomba(x, y);
        } else if (type.equals("Koopa")) {
            return new Koopa(x, y);
        }
        else if (type.equals("Minotaur")){
            return new Minotaur(x, y, Integer.parseInt(split.get(4)), Integer.parseInt(split.get(5)));
        }else if (type.equals("Archer")) {
            return new Archer(x, y, Integer.parseInt(split.get(4)), Integer.parseInt(split.get(5)));
        }
        else if (type.equals("Sorcerer")) {
            return new Sorcerer(x, y, Integer.parseInt(split.get(4)), Integer.parseInt(split.get(5)));
        }
        else {
            return null;
        }
    }

    /**
     * Creates and returns a {@code StaticGameObject} as defined by the input list
     * of properties. The format of the input {@code ArrayList} is:
     * <p>
     * "StaticGameObject,SubType,x,y,..."
     * <p>
     * Where SubType is the name of the class, x and y are the location of the
     * object, and any following items are additional constructor parameters.
     * 
     * @param game  the game this object will be a member of
     * @param level the level this object will be a member of
     * @param split the split line from the csv file describing the object
     * @return the object that is described by {@code split}
     */
    public static StaticGameObject readStaticObject(Game game, Level level, ArrayList<String> split) {
        double x = Double.parseDouble(split.get(2));
        double y = Double.parseDouble(split.get(3));
        String type = split.get(1);
        if (type.equals("Wall")) {
            return new Wall(x, y);
        } else if (type.equals("Goal")) {
            return new Goal(x, y, game);
        } else if (type.equals("InfoNode")) {
            return new InfoNode(x, y, split.get(4));
        } else if (type.equals("Block") || type.equals("Bricks") || type.equals("Ground")) {
            return new Block(x, y, type);
        } else if (type.equals("QuestionBlock")) {
            return new QuestionBlock(x, y);
        } else if (type.equals("HiddenBlock")) {
            return new HiddenBlock(x, y);
        } else if (type.equals("PipeEnd")) {
            return new PipeEnd(x, y);
        } else if (type.equals("PipeStem")) {
            return new PipeStem(x, y);
        } else if (type.equals("Flag")) {
            return new Flag(x, y, game);
        } else if (type.equals("AxePickup")) {
            return new AxePickup(x, y, game);
        } else if (type.equals("MagicPickup")){
            return new MagicPickup(x, y, game);
        } else if (type.equals("PotionPickup")) {
            return new PotionPickup(x, y, Integer.parseInt(split.get(4)), game);
        }
        else if (type.equals("Spike")) {
            return new Spike(x, y);
        }
        else if (type.equals("Potion")) {
            return new Potion(x, y, Integer.parseInt(split.get(4)));
        }
        else if (type.equals("Marker")) {
            return new Marker(x, y, split.get(4));
        }
        else if (type.equals("DirectionalWall")) {
            return new DirectionalWall(x, y, level);
        }
        else {
            return null;
        }
    }

    /**
     * Creates and returns a {@code Background} object as defined by the input list
     * of properties. If the first item in {@code split} must be either
     * <i>BackgroundImage</i> or <i>BackgroundTile</i>.
     * <p>
     * If the line defines a <i>BackgroundImage</i>, the following entries must all
     * be strings specifying the filepaths of those backgrounds within the
     * background directory. If multiple images are specified, they will be layered
     * back to front with evenly spaced parallax ratios from 0.0 to 1.0.
     * <p>
     * If the line defines a <i>BackgroundTile</i>, the following entries must be a
     * string specifying the filepath of that tile sprite sheet within the sprites
     * directory and two ints for the column and row within that sprite sheet, in
     * that order.
     * 
     * @param split the split line from the csv file describing the object
     * @return the background that is described by {@code split}
     */
    public static Background readBackground(ArrayList<String> split) {
        if (split.get(0).equals("BackgroundImage")) {
            return new Background(new ArrayList<>(split.subList(1, split.size())));
        } else {
            int column = Integer.parseInt(split.get(2));
            int row = Integer.parseInt(split.get(3));
            return new Background(split.get(1), column, row);
        }
    }

}
