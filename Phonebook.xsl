<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <html>
            <body>
                <table border="1">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Initials</th>
                            <th>Email</th>
                            <th>Phone</th>
                        </tr>
                    </thead>
                    <xsl:for-each select="PHONEBOOK/PERSON">
                        <tr>
                            <td>
                                <xsl:value-of select="@ID"/>
                            </td>
                            <td>
                                <xsl:value-of select="NAME"/>
                            </td>
                            <td>
                                <xsl:value-of select="NAME/@INITIALS"/>
                            </td>
                            <td>
                                <xsl:value-of select="EMAIL"/>
                            </td>
                            <td>
                                <xsl:value-of select="TELEPHONE"/>
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>