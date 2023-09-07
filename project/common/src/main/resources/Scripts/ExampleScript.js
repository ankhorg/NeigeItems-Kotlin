function main() {
    if (typeof this.player != "undefined") {
        return this.vars("<strings-1>") + this.player.getName()
    } else {
        return this.vars("<strings-1>")
    }
}
