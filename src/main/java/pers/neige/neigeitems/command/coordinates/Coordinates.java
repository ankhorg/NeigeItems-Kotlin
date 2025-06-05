package pers.neige.neigeitems.command.coordinates;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.val;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.command.CommandUtils;

public class Coordinates {
    private final @Nullable Coordinate x;
    private final @Nullable Coordinate y;
    private final @Nullable Coordinate z;
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

    public static Coordinates parse(StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();
        val x = readCoordinate(reader);
        if (reader.canRead() && reader.peek() == ' ') reader.skip();
        val y = readCoordinate(reader);
        if (reader.canRead() && reader.peek() == ' ') reader.skip();
        val z = readCoordinate(reader);
        return new Coordinates(x, y, z);
    }

    private static Coordinate readCoordinate(StringReader reader) throws CommandSyntaxException {
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
        val value = (!reader.canRead() || reader.peek() == ' ') ? Double.valueOf(0.0) : CommandUtils.readDouble(reader);
        return new Coordinate(type, value);
    }

    public @Nullable Location getLocation(World world, CommandSender sender, Player target) {
        if (noResult) return null;
        if (sender instanceof Player || target != null) {
            Player player;
            if (target != null) {
                player = target;
            } else {
                player = (Player) sender;
            }
            if (local) {
                val location = player.getEyeLocation();
                val leftwards = x.getValue();
                val upwards = y.getValue();
                val forwards = z.getValue();

                val rotation = location.getDirection();
                val anchor = location.toVector();

                val forward = rotation.clone().normalize();
                val left = forward.clone().crossProduct(new Vector(0, -1, 0)).normalize();
                val up = left.clone().crossProduct(forward).normalize();

                val position = forward.multiply(forwards)
                        .add(up.multiply(-upwards))
                        .add(left.multiply(leftwards));

                position.add(anchor);

                return position.toLocation(world);
            } else {
                val location = player.getLocation();
                return new Location(world, x.get(location.getX()), y.get(location.getY()), z.get(location.getZ()));
            }
        } else {
            return new Location(world, x.get(0), y.get(0), z.get(0));
        }
    }
}
