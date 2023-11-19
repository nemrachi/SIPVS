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
"XML file was saved."
```
```
Status 200
```
Example error output:
```
"Save error: [error message]"
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
"Validation successful. XML is valid against the XSD."
```
```
Status 200
```
Example error output:
```
"Validation error: [error message]"
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
"HTML file was saved."
```
```
Status 200
```
Example error output:
```
"Transformation error: [error message]"
```
```
Status 500
```

## GET: http://localhost:8080/api/zadanie2/getxml

Example input:
```
N/A
```
Example output:
```
XML content
```
```
Status 200
```
Example error output:
```
?
```
```
Status 500
```

## GET: http://localhost:8080/api/zadanie2/getxsd

Example input:
```
N/A
```
Example output:
```
XSD content
```
```
Status 200
```
Example error output:
```
?
```
```
Status 500
```

## GET: http://localhost:8080/api/zadanie2/getxsl

Example input:
```
N/A
```
Example output:
```
XSL content
```
```
Status 200
```
Example error output:
```
?
```
```
Status 500
```

## GET: http://localhost:8080/api/zadanie2/generatePdfFromXml

Example input:
```
N/A
```
Example output:
```
PDF content
```
```
Status 200
```
Example error output:
```
?
```
```
Status 500
```

## GET: http://localhost:8080/api/zadanie3/timestamp

Example input:
```
N/A
```
Example output:
```
XML response?
```
```
Status 200
```
Example error output:
```
?
```
```
Status 500
```

---
### BE
Java 8, Spring Boot

### FE
React, Axios 

#### How to run FE
```
cd sipvs-fe
npm install
npm run dev
```

## Podpisovac navod

- stiahnut a nainstalovat D.Lanucher v2.x
  - pozriet si systemove poziadavky k prislusnemu operacnemu systemu
- pridat plugin D.Bridge 2 do prehliadaca
- **? ako to spojazdnit ?**

---
# 3.zadanie - casova peciatka
- nové tlačidlo – prevod EPES na **T formu**
  - Vstup – pôvodný podpis XAdES EPES
  - Výstup – rozšírený podpis o časovú pečiatku XAdES T uložený v novom súbore
- Tipy
  - inšpirovať sa/použiť kód zo Sample priečinka (`sipvs-be/src/main/resources/sample-zadanie3`)
  - pri manipulácii s XML nastaviť nástroje (parser), aby **NEmodifikoval** pôvodný obsah
  - dôkladne si preštudovať profil
    - identifikácia čo sa pečiatkuje
