package kmp.project.gameoflife.data.utils

/**
 * Utility class to parse RLE (Run-Length Encoded) format for Game of Life.
 */
object RleParser {

    /**
     * Extracts name from RLE metadata (#N)
     */
    fun getName(rle: String): String? {
        return rle.lines()
            .firstOrNull { it.startsWith("#N") }
            ?.removePrefix("#N")
            ?.trim()
    }

    /**
     * Extracts max dimension (gridSize) from RLE header (x=..., y=...)
     */
    fun getGridSize(rle: String): Int {
        val header = rle.lines().firstOrNull { it.startsWith("x") } ?: return 10
        val xMatch = "x\\s*=\\s*(\\d+)".toRegex().find(header)
        val yMatch = "y\\s*=\\s*(\\d+)".toRegex().find(header)
        val x = xMatch?.groupValues?.get(1)?.toInt() ?: 10
        val y = yMatch?.groupValues?.get(1)?.toInt() ?: 10
        return maxOf(x, y)
    }

    /**
     * Decodes an RLE string into a list of cell coordinates (Pair<Int, Int>).
     * Format: 'o' for alive, 'b' for dead, '$' for new line, '!' for end.
     * Numbers before tags represent repetition.
     */
    fun decode(rle: String): List<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()
        var x = 0
        var y = 0
        var number = ""

        // Remove header (x=..., y=..., rule=...) and comments
        val cleanRle = rle.lines()
            .filter { !it.startsWith("#") && !it.startsWith("x") }
            .joinToString("")
            .replace("\\s".toRegex(), "")

        for (char in cleanRle) {
            when {
                char.isDigit() -> number += char
                char == 'o' -> {
                    val count = if (number.isEmpty()) 1 else number.toInt()
                    repeat(count) {
                        result.add(Pair(x, y))
                        x++
                    }
                    number = ""
                }
                char == 'b' -> {
                    val count = if (number.isEmpty()) 1 else number.toInt()
                    x += count
                    number = ""
                }
                char == '$' -> {
                    val count = if (number.isEmpty()) 1 else number.toInt()
                    y += count
                    x = 0
                    number = ""
                }
                char == '!' -> break
            }
        }
        return result
    }

    /**
     * Encodes a list of cell coordinates into an RLE string.
     */
    fun encode(cells: List<Pair<Int, Int>>, name: String = "Unknown"): String {
        if (cells.isEmpty()) return "#N $name\n!"
        
        val minX = cells.minOf { it.first }
        val minY = cells.minOf { it.second }
        val maxX = cells.maxOf { it.first }
        val maxY = cells.maxOf { it.second }
        
        val normalizedCells = cells.map { it.first - minX to it.second - minY }.toSet()
        val width = maxX - minX + 1
        val height = maxY - minY + 1
        
        val sb = StringBuilder()
        sb.append("#N $name\n")
        sb.append("x = $width, y = $height, rule = B3/S23\n")
        
        for (y in 0 until height) {
            var x = 0
            while (x < width) {
                val isAlive = normalizedCells.contains(x to y)
                val type = if (isAlive) 'o' else 'b'
                var count = 0
                while (x < width && normalizedCells.contains(x to y) == isAlive) {
                    count++
                    x++
                }
                if (count > 1) sb.append(count)
                sb.append(type)
            }
            if (y < height - 1) sb.append('$') else sb.append('!')
        }
        println(sb.toString())
        return sb.toString()
    }
}
