<?xml version="1.0" encoding="utf-8" standalone="no" ?>
<xsl:stylesheet xmlns="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="xml" encoding="utf-8" indent="yes"
                doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
                doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"/>
    <xsl:template match="/">
        <xsl:element name="body">
            <xsl:element name="h1">
                Loan a book
            </xsl:element>
            <xsl:element name="table">
                <xsl:element name="tr">
                    <xsl:element name="td">
                        Loan ID
                    </xsl:element>
                    <xsl:element name="td">
                        <xsl:element name="input">
                            <xsl:attribute name="type">text</xsl:attribute>
                            <xsl:attribute name="disabled">disabled</xsl:attribute>
                            <xsl:attribute name="value">L000002</xsl:attribute>
                        </xsl:element>
                    </xsl:element>
                    <xsl:element name="tr">
                        <xsl:element name="td">
                            Librarian ID
                        </xsl:element>
                        <xsl:element name="td">
                            <xsl:element name="input">
                                <xsl:attribute name="type">number</xsl:attribute>
                                <xsl:attribute name="disabled">disabled</xsl:attribute>
                                <xsl:attribute name="value">2</xsl:attribute>
                            </xsl:element>
                        </xsl:element>
                        <xsl:element name="tr">
                            <xsl:element name="td">
                                Borrower Card Number
                            </xsl:element>
                            <xsl:element name="td">
                                <xsl:element name="input">
                                    <xsl:attribute name="type">text</xsl:attribute>
                                    <xsl:attribute name="disabled">disabled</xsl:attribute>
                                    <xsl:attribute name="value">ABCDEFG</xsl:attribute>
                                </xsl:element>
                            </xsl:element>
                            <xsl:element name="tr">
                                <xsl:element name="td">
                                    Date Of Loan
                                </xsl:element>
                                <xsl:element name="td">
                                    <xsl:element name="input">
                                        <xsl:attribute name="type">date</xsl:attribute>
                                        <xsl:attribute name="disabled">disabled</xsl:attribute>
                                        <xsl:attribute name="value">2023-10-05</xsl:attribute>
                                    </xsl:element>
                                </xsl:element>
                                <xsl:element name="tr">
                                    <xsl:element name="td">
                                        Due Date
                                    </xsl:element>
                                    <xsl:element name="td">
                                        <xsl:element name="input">
                                            <xsl:attribute name="type">date</xsl:attribute>
                                            <xsl:attribute name="disabled">disabled</xsl:attribute>
                                            <xsl:attribute name="value">2023-11-05</xsl:attribute>
                                        </xsl:element>
                                    </xsl:element>
                                    <xsl:element name="tr">
                                        <xsl:element name="td">
                                            <xsl:element name="h3">
                                                Books
                                            </xsl:element>
                                        </xsl:element>
                                    </xsl:element>
                                    <xsl:element name="tr">
                                        <xsl:element name="td">
                                            ISBN
                                        </xsl:element>
                                        <xsl:element name="td">
                                            <xsl:element name="input">
                                                <xsl:attribute name="type">text</xsl:attribute>
                                                <xsl:attribute name="disabled">disabled</xsl:attribute>
                                                <xsl:attribute name="value">9780451524935</xsl:attribute>
                                            </xsl:element>
                                        </xsl:element>
                                        <xsl:element name="tr">
                                            <xsl:element name="td">
                                                ISBN
                                            </xsl:element>
                                            <xsl:element name="td">
                                                <xsl:element name="input">
                                                    <xsl:attribute name="type">text</xsl:attribute>
                                                    <xsl:attribute name="disabled">disabled</xsl:attribute>
                                                    <xsl:attribute name="value">9780486282114</xsl:attribute>
                                                </xsl:element>
                                            </xsl:element>
                                        </xsl:element>
                                    </xsl:element>
                                </xsl:element>
                            </xsl:element>
                        </xsl:element>
                    </xsl:element>
                </xsl:element>
            </xsl:element>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>