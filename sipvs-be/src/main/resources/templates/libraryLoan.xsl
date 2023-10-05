<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:l="http://www.library.com/loan"
                version="1.0"
                exclude-result-prefixes="l">
    <xsl:output method="html" indent="yes"/>
    <xsl:template match="l:libraryLoan">
        <html>
            <body>
                <form action="welcome.jsp" method="post"
                      style="display: block;
                            max-width: min-content;
                            text-align: center;
                            margin-left: auto;
                            margin-right: auto;
                            margin-top: 7%;
                            padding: 20px;
                            border-radius: 7px;
                            box-shadow: 1px 7px 13px 7px lightgrey;
                            ">
                    <h1>Vypozicka</h1>
                    <table>
                        <tr>
                            <td>Loan ID</td>
                            <td>
                                <input type="text" disabled="disabled">
                                    <xsl:attribute name="value">
                                        <xsl:value-of select="@loan_id"/>
                                    </xsl:attribute>
                                </input>
                            </td>
                        </tr>
                        <tr>
                            <td>Librarian ID</td>
                            <td>
                                <input type="number" disabled="disabled">
                                    <xsl:attribute name="value">
                                        <xsl:value-of select="l:librarianId"/>
                                    </xsl:attribute>
                                </input>
                            </td>
                        </tr>
                        <tr>
                            <td>Borrower Card Number</td>
                            <td>
                                <input type="text" disabled="disabled">
                                    <xsl:attribute name="value">
                                        <xsl:value-of select="l:borrower/l:cardNumber"/>
                                    </xsl:attribute>
                                </input>
                            </td>
                        </tr>
                        <tr>
                            <td>Date Of Loan</td>
                            <td>
                                <input type="date" disabled="disabled">
                                    <xsl:attribute name="value">
                                        <xsl:value-of select="l:dateOfLoan"/>
                                    </xsl:attribute>
                                </input>
                            </td>
                        </tr>
                        <tr>
                            <td>Due Date</td>
                            <td>
                                <input type="date" disabled="disabled">
                                    <xsl:attribute name="value">
                                        <xsl:value-of select="l:dueDate"/>
                                    </xsl:attribute>
                                </input>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h3>Knihy</h3>
                            </td>
                        </tr>
                        <xsl:for-each select="l:borrowedBooks/l:book">
                            <tr>
                                <td>ISBN</td>
                                <td>
                                    <input type="text" disabled="disabled">
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="l:isbn"/>
                                        </xsl:attribute>
                                    </input>
                                </td>
                            </tr>
                        </xsl:for-each>
                    </table>
                </form>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>