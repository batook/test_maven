<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <html>
            <body>
                <table border="1">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Barcode</th>
                            <th>Title</th>
                            <th>Path</th>
                        </tr>
                    </thead>
                    <xsl:for-each select="REPORT/ITEMS/ITEM">
                        <tr>
                            <td>
                                <xsl:value-of select="@ID"/>
                            </td>
                            <xsl:call-template name="BARCODE"/>
                            <td>
                                <xsl:value-of select="TITLE"/>
                            </td>
                            <td>
                                <xsl:value-of select="COVER_PATH"/>
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
    <xsl:template name="BARCODE">
        <td>
            <xsl:value-of select="BARCODES/BARCODE"/>
        </td>
    </xsl:template>
</xsl:stylesheet>