# SIPVS project

### BE
Java 8

Spring Boot

### FE
?

---
# 1.zadanie
- tvorba formulára – každá skupina si zvolí sama
- návrh XSD
    - [x] aspoň 3 dátové typy (`int, string, date`)
    - [x] opakujúca sa sekcia (`borrowedBooks`)
    - [x] aspoň 1 atribút (`loan_id`)
    - [x] vymyslieť menný priestor - targetNamespace (`http://www.library.com/loan`)
    - !!!nekomplikovať!!!
- návrh XSL
    - [x] do HTML
    - [ ] ? ~~ak je z reálneho sveta, zabezpečiť maximálnu podobu s papierom~~ ?
    - [x] readonly bez aktívnych prvkov
- implementácia web aplikácie (`FE`)
    - slúži na zber údajov – vyplnenie formulára
    - zohľadňuje pravidlá v XSD (`validacia na FE pri vyplnani`)
    - [ ] aplikuje spomenuté výhody elektronizácie
        - kontrola, komfort, dopočítavanie, predvypĺňanie, ....
    - aplikácia bude mať 3 tlačidlá (`BE endpoints`)
        - [ ] Ulož XML
            - uloží vytvorené xml do súboru
        - [ ] Over XML voči XSD
            - overí uložené xml voči vytvorenému xsd
            - na overenie použije nástroje a triedy jazyka/prostredia
            - výstup validácie zobrazí aj s detailom prípadnej chyby
        - [ ] Transformuj XML do HTML
            - transformuje uložené xml pomocou vytvorenej xsl
            - na transformáciu použije nástroje a triedy jazyka/prostredia
            - výstup uloží do súboru