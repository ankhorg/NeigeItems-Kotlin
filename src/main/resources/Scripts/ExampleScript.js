function main() {
    if (typeof player != "undefined") {
        return vars("<strings-1>") + player.getName()
    } else {
        return vars("<strings-1>")
    }
}

load = function() {return this}
load()
