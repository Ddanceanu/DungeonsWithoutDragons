package com.dungeons.use_case

import com.dungeons.model.GameMap
import java.io.File

object ExportMapToHtmlUseCase {

    operator fun invoke(map: GameMap) {
        val htmlContent = StringBuilder()

        htmlContent.append(
            """
            <html>
            <head>
                <title>Game Map</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        height: 100vh;
                        margin: 0;
                        background-color: #f5f5f5;
                    }
                    table {
                        border-collapse: collapse;
                        width: 50%;
                        max-width: 600px;
                        text-align: center;
                        background-color: white;
                        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                    }
                    th, td {
                        border: 1px solid #dddddd;
                        padding: 10px;
                        font-size: 18px;
                        max-width: 20ch;
                        word-wrap: break-word;
                    }
                    tr:nth-child(even) {
                        background-color: #f2f2f2;
                    }
                    tr:hover {
                        background-color: #e0e0e0;
                    }
                    caption {
                        font-size: 24px;
                        font-weight: bold;
                        margin-bottom: 10px;
                    }
                </style>
            </head>
            <body>
            """.trimIndent(),
        )

        htmlContent.append("<table>\n<caption>Game Map</caption>\n\n")

        for (row in map.mapMatrix) {
            htmlContent.append("<tr>")
            for (cell in row) {
                htmlContent.append("<td>$cell</td>")
            }
            htmlContent.append("</tr>\n")
        }

        htmlContent.append("</table>\n</body>\n</html>")

        File("Map.html").writeText(htmlContent.toString())
    }
}