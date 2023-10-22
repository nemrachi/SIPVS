<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:l="http://library.com/loan">

    <xsl:template  match="l:libraryLoan">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4" page-height="29.7cm" page-width="21cm"
                                       margin-top="1cm" margin-bottom="1cm" margin-left="2cm" margin-right="2cm">
                    <fo:region-body margin-top="1.5cm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="A4">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block font-size="20pt" text-align="center" space-after.optimum="20pt">
                        <fo:inline font-weight="bold">Library loan</fo:inline>
                    </fo:block>

                    <fo:block space-after.optimum="10pt">
                        <fo:inline font-weight="bold">Loan ID: </fo:inline>
                        <fo:inline><xsl:value-of select="@loan_id"/></fo:inline>
                    </fo:block>

                    <fo:block space-after.optimum="10pt">
                        <fo:inline font-weight="bold">Librarian ID: </fo:inline>
                        <fo:inline><xsl:value-of select="l:librarianId"/></fo:inline>
                    </fo:block>

                    <fo:block space-after.optimum="10pt">
                        <fo:inline font-weight="bold">Borrower Card Number: </fo:inline>
                        <fo:inline><xsl:value-of select="l:borrower/l:cardNumber"/></fo:inline>
                    </fo:block>

                    <fo:block space-after.optimum="10pt">
                        <fo:inline font-weight="bold">Date Of Loan: </fo:inline>
                        <fo:inline><xsl:value-of select="l:dateOfLoan"/></fo:inline>
                    </fo:block>

                    <fo:block space-after.optimum="10pt">
                        <fo:inline font-weight="bold">Due Date: </fo:inline>
                        <fo:inline><xsl:value-of select="l:dueDate"/></fo:inline>
                    </fo:block>

                    <fo:block space-after.optimum="10pt">
                        <fo:inline font-weight="bold">Borrowed books:</fo:inline>
                    </fo:block>

                    <!-- Iterate through borrowed books -->
                    <xsl:for-each select="l:borrowedBooks/l:book">
                        <fo:block start-indent="30pt" space-after.optimum="10pt">
                            <fo:inline font-weight="bold">ISBN: </fo:inline>
                            <fo:inline ><xsl:value-of select="l:isbn"/></fo:inline>
                        </fo:block>
                    </xsl:for-each>

                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>
