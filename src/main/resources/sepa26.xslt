<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="3.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:p="http://univ.fr/sepa26" 
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="p xs">

    <xsl:output method="html" encoding="UTF-8" indent="yes" doctype-system="about:legacy-compat"/>

    <xsl:template match="/">
        <html lang="fr">
            <head>
                <meta charset="UTF-8"/>
                <title>Transactions SEPA</title>
                <link rel="stylesheet" href="/css/sepa26.css"/>
            </head>
            <body>
                <header class="header-doc">
                    <h1>Transactions SEPA</h1>
                    <p class="date-jour">
    Date émission : 
    <xsl:variable name="aujourdhui" select="current-date()"/>
    
    <xsl:variable name="jour" select="day-from-date($aujourdhui)"/>
    <xsl:variable name="moisNum" select="month-from-date($aujourdhui)"/>
    <xsl:variable name="annee" select="year-from-date($aujourdhui)"/>
    
    <xsl:variable name="moisTexte">
        <xsl:choose>
            <xsl:when test="$moisNum = 1">Janv</xsl:when>
            <xsl:when test="$moisNum = 2">Févr</xsl:when>
            <xsl:when test="$moisNum = 3">Mars</xsl:when>
            <xsl:when test="$moisNum = 4">Avr</xsl:when>
            <xsl:when test="$moisNum = 5">Mai</xsl:when>
            <xsl:when test="$moisNum = 6">Juin</xsl:when>
            <xsl:when test="$moisNum = 7">Juil</xsl:when>
            <xsl:when test="$moisNum = 8">Août</xsl:when>
            <xsl:when test="$moisNum = 9">Sept</xsl:when>
            <xsl:when test="$moisNum = 10">Oct</xsl:when>
            <xsl:when test="$moisNum = 11">Nov</xsl:when>
            <xsl:when test="$moisNum = 12">Déc</xsl:when>
        </xsl:choose>
    </xsl:variable>

    <xsl:value-of select="$jour"/>
    <xsl:text> </xsl:text>
    <xsl:value-of select="$moisTexte"/>
    <xsl:text> </xsl:text>
    <xsl:value-of select="$annee"/>
</p>
                </header>

                <main>
                   <section class="sommaire">
                        <h2>Liste des transactions</h2>
                        <ol>
                            <xsl:for-each select="//p:DrctDbtTxInf">
                                
                                <xsl:sort select="p:InstdAmt" data-type="number" order="descending"/>
                                
                                <li>
                                    montant = <xsl:value-of select="p:InstdAmt"/> <xsl:value-of select="p:InstdAmt/@Ccy"/><br/>
                                    référence : <xsl:value-of select="p:PmtId"/>
                                </li>
                            </xsl:for-each>
                        </ol>
                    </section>

                    <xsl:apply-templates select="//p:DrctDbtTxInf"/>
                </main>

                <footer class="footer-doc">
                    Document émis par Ahcène AMOUCHAS
                    <br/>
                    <a href="/sepa26/resume/html">← Retour à la liste des transactions</a>
                </footer>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="p:DrctDbtTxInf">
        <section class="transaction-bloc">
            
            <h2>
                <xsl:text>Transaction </xsl:text>
                
                <xsl:value-of select="position()"/>
                
                <xsl:text>/</xsl:text>
                
                <xsl:value-of select="last()"/>
                
                <xsl:text> : </xsl:text>
                <xsl:value-of select="p:PmtId"/>
            </h2>
            
            <div class="transaction-details">
                <table class="transaction-table">
                    <tr>
                        <td class="label">Montant</td>
                        <td class="valeur">
                            <xsl:value-of select="p:InstdAmt"/>
                            <xsl:text> </xsl:text>
                            <xsl:value-of select="p:InstdAmt/@Ccy"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">Date</td>
                        <td class="valeur">
                            <xsl:variable name="rawDate" select="p:DrctDbtTx/p:MndtRltdInf/p:DtOfSgntr"/>
                            
                            <xsl:variable name="annee" select="substring($rawDate, 1, 4)"/>
                            <xsl:variable name="mois" select="substring($rawDate, 6, 2)"/>
                            <xsl:variable name="jour" select="substring($rawDate, 9, 2)"/>
                            
                            <xsl:variable name="moisTexte">
                                <xsl:choose>
                                    <xsl:when test="$mois = '01'">Janv</xsl:when>
                                    <xsl:when test="$mois = '02'">Févr</xsl:when>
                                    <xsl:when test="$mois = '03'">Mars</xsl:when>
                                    <xsl:when test="$mois = '04'">Avr</xsl:when>
                                    <xsl:when test="$mois = '05'">Mai</xsl:when>
                                    <xsl:when test="$mois = '06'">Juin</xsl:when>
                                    <xsl:when test="$mois = '07'">Juil</xsl:when>
                                    <xsl:when test="$mois = '08'">Août</xsl:when>
                                    <xsl:when test="$mois = '09'">Sept</xsl:when>
                                    <xsl:when test="$mois = '10'">Oct</xsl:when>
                                    <xsl:when test="$mois = '11'">Nov</xsl:when>
                                    <xsl:when test="$mois = '12'">Déc</xsl:when>
                                </xsl:choose>
                            </xsl:variable>

                            <xsl:value-of select="$jour"/>
                            <xsl:text> </xsl:text>
                            <xsl:value-of select="$moisTexte"/>
                            <xsl:text> </xsl:text>
                            <xsl:value-of select="$annee"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">Transac</td>
                        <td class="valeur"><xsl:value-of select="p:DrctDbtTx/p:MndtRltdInf/p:MndtId"/></td>
                    </tr>

                    <tr>
                        <td colspan="2" class="section-title">Débiteur</td>
                    </tr>
                    <tr>
                        <td class="label">Nom</td>
                        <td class="valeur"><xsl:value-of select="p:Dbtr/p:Nm"/></td>
                    </tr>
                    
                    <xsl:choose>
                        <xsl:when test="p:DbtrAgt/p:FinInstnId/p:BIC">
                            <tr>
                                <td class="label">BIC</td>
                                <td class="valeur"><xsl:value-of select="p:DbtrAgt/p:FinInstnId/p:BIC"/></td>
                            </tr>
                        </xsl:when>
                        <xsl:otherwise>
                            <tr>
                                <td class="label">Agent</td>
                                <td class="valeur"><xsl:value-of select="p:DbtrAgt/p:FinInstnId/p:Othr/p:Id"/></td>
                            </tr>
                        </xsl:otherwise>
                    </xsl:choose>

                    <tr>
                        <td class="label">IBAN</td>
                        <td class="valeur"><xsl:value-of select="p:DbtrAcct/p:Id/p:IBAN"/></td>
                    </tr>

                    <xsl:if test="p:RmtInf">
                        <tr>
                            <td colspan="2" class="section-title">Comment</td>
                        </tr>
                        <tr>
                            <td colspan="2" class="valeur"><xsl:value-of select="p:RmtInf"/></td>
                        </tr>
                    </xsl:if>
                </table>
            </div>
        </section>
    </xsl:template>

</xsl:stylesheet>