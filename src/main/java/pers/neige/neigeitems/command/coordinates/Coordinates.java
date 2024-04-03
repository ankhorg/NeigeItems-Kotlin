package pers.neige.neigeitems.command.coordinates;

import com.mojang.brigadier.StringReader;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.command.CommandUtils;

public class Coordinates {
    @Nullable
    private final Coordinate x;
    @Nullable
    private final Coordinate y;
    @Nullable
    private final Coordinate z;
    private final boolean noResult;
    private final boolean local;

    public Coordinates(@Nullable Coordinate x, @Nullable Coordinate y, @Nullable Coordinate z) {
        this.x = x;
        this.y = y;
        this.z = z;
        if (this.x == null || this.y == null || this.z == null) {
            noResult = true;
            local = false;
        } else {
            noResult = false;
            local = (this.x.getType() == LocationType.LOCAL && this.y.getType() == LocationType.LOCAL && this.z.getType() == LocationType.LOCAL);
        }
    }

    public static Coordinates parse(StringReader reader) {
        int i = reader.getCursor();
        Coordinate x = readCoordinate(reader);
        if (reader.canRead() && reader.peek() == ' ') reader.skip();
        Coordinate y = readCoordinate(reader);
        if (reader.canRead() && reader.peek() == ' ') reader.skip();
        Coordinate z = readCoordinate(reader);
        return new Coordinates(x, y, z);
    }

    private static Coordinate readCoordinate(StringReader reader) {
        if (!reader.canRead()) {
            return new Coordinate(LocationType.ABSOLUTE, 0.0);
        }
        LocationType type;
        if (reader.peek() == '~') {
            reader.skip();
            type = LocationType.RELATIVE;
        } else if (reader.peek() == '^') {
            reader.skip();
            type = LocationType.LOCAL;
        } else {
            type = LocationType.ABSOLUTE;
        }
        Double value = (!reader.canRead() || reader.peek() == ' ') ? Double.valueOf(0.0) : CommandUtils.readDouble(reader);
        if (value == null) return null;
        return new Coordinate(type, value);
    }

    @Nullable
    public Location getLocation(World world, CommandSender sender, Player target) {
        if (noResult) return null;
        if (sender instanceof Player || target != null) {
            Player player;
            if (target != null) {
                player = target;
            } else {
                player = (Player) sender;
            }
            if (local) {
                Location location = player.getEyeLocation();
                double leftwards = x.getValue();
                double upwards = y.getValue();
                double forwards = z.getValue();

                Vector rotation = location.getDirection();
                Vector anchor = location.toVector();

                Vector forward = rotation.clone().normalize();
                Vector left = forward.clone().crossProduct(new Vector(0, -1, 0)).normalize();
                Vector up = left.clone().crossProduct(forward).normalize();

                Vector position = forward.multiply(forwards)
                        .add(up.multiply(-upwards))
                        .add(left.multiply(leftwards));

                position.add(anchor);

                return position.toLocation(world);
            } else {
                Location location = player.getLocation();
                return new Location(world, x.get(location.getX()), y.get(location.getY()), z.get(location.getZ()));
            }
        } else {
            return new Location(world, x.get(0), y.get(0), z.get(0));
        }
    }
}
