# SIPVS project

## Endpoints

## POST: http://localhost:8080/api/zadanie1/save

Example input:
```
{
  "loanId": "L000002",
  "librarianId": 2,
  "borrower": {
    "cardNumber": "ABCDEFG"
  },
  "dateOfLoan": "2023-10-05",
  "dueDate": "2023-11-05",
  "loanedBooks": [
    {
      "isbn": "9780451524935"
    },
    {
      "isbn": "9780486282114"
    }
  ]
}
```
Example output:
```
XML file was saved.

Status 200
```
Example error output:
```
Save error: error message
```
```
Status 500
```
Data correct format:

| Element | Format | Example |
|---|---|---|
| loan_id | [A-Z][0-9]{6} | A123456 |
| librarianId | integer | 1 |
| dateOfLoan | yyyy-MM-dd | 2023-01-01 |
| dueDate | yyyy-MM-dd | 2023-01-01 |
| cardNumber | [A-Z0-9]{7} | ABC1DEF |
| isbn | [0-9]{10} \| [0-9]{13} | 0123456789, 0123456789012 |

## GET: http://localhost:8080/api/zadanie1/validate

Example input:
```
N/A
```
Example output:
```
Validation successful. XML is valid against the XSD.
```
```
Status 200
```
Example error output:
```
Validation error: error message
```
```
Status 422
```

## GET: http://localhost:8080/api/zadanie1/transform

Example input:
```
N/A
```
Example output:
```
HTML file was saved
```
```
Status 200
```
Example error output:
```
Transformation error: error message
```
```
Status 500
```
---

### BE
Java 8

Spring Boot

### FE

React

Axios 


How to run FE

```
cd sipvs-fe
npm install
npm run dev
```

---
# 1.zadanie
- tvorba formulára – každá skupina si zvolí sama
- návrh XSD
    - [x] aspoň 3 dátové typy (`int, string, date`)
    - [x] opakujúca sa sekcia (`borrowedBooks`)
    - [x] aspoň 1 atribút (`loan_id`)
    - [x] vymyslieť menný priestor - targetNamespace (`http://library.com/loan`)
    - !!!nekomplikovať!!!
- návrh XSL
    - [x] do HTML
    - [ ] ? ~~ak je z reálneho sveta, zabezpečiť maximálnu podobu s papierom~~ ?
    - [x] readonly bez aktívnych prvkov
- implementácia web aplikácie (`FE`)
    - slúži na zber údajov – vyplnenie formulára
    - zohľadňuje pravidlá v XSD (`validacia na FE pri vyplnani`)
    - [X] aplikuje spomenuté výhody elektronizácie
        - kontrola, komfort, dopočítavanie, predvypĺňanie, ....
    - aplikácia bude mať 3 tlačidlá (`BE endpoints`)
        - [x] Ulož XML
            - uloží vytvorené xml do súboru
        - [X] Over XML voči XSD
            - overí uložené xml voči vytvorenému xsd
            - na overenie použije nástroje a triedy jazyka/prostredia
            - výstup validácie zobrazí aj s detailom prípadnej chyby
        - [X] Transformuj XML do HTML
            - transformuje uložené xml pomocou vytvorenej xsl
            - na transformáciu použije nástroje a triedy jazyka/prostredia
            - výstup uloží do súboru

---
# 2.zadanie
- nové tlačidlo
  - [ ] pridať nové tlačidlo na existujúci formulár s názvom “Podpísať”
  - [ ] zavolať podpisovač a jedným podpisom podpísať
    - objekt xml dáta s xsd a xslt
    - objekt pdf
  - [ ] uložiť výstup z podpisovača do súboru s xml koncovkou (```xades.xml```)
    - xades podpis vo formate xades epes
- zavolať podpisovač a jedným podpisom podpísať
  - https://www.slovensko.sk/sk/na-stiahnutie/informacie-pre-integratorov-ap časť D.Bridge JS, v1.x, pozrieť Integračná príručka D.Bridge JS, v1.x a v nej
    - str.13 objekt ditec.dSigXadesJs
    - metódy addXmlObject2, addPdfObject, sign/sign11/sign20
  - detail parametrov metód popísaný v časti Dokumentácia pre integrátorov klientskych aplikácií pre KEP balíka D.Suite/eIDAS
- uložiť výstup z podpisovača do súboru s xml koncovkou
  - xades podpis vo formate xades epes
    - metóda getSignedXmlWithEnvelope