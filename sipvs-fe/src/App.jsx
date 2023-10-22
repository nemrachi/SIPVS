import React, {useState} from 'react';
import { validateData, transformData, signData, confirmData } from './api';


import SignComponent from './SignComponent';

function App() {
    const EMPTY_LOAN = {
        loanId: "",
        librarianId: 0,
        borrower: {
            cardNumber: ""
        },
        dateOfLoan: "",
        dueDate: "",
        loanedBooks: [{"isbn": ""}]
    }
    const [loan, setLoan] = useState(EMPTY_LOAN);
    const handleChange = (e, nested) => {
        const value = e.target.value;
        if (nested) {
            setLoan({...loan, [nested]: {...loan[nested], [e.target.name]: value}});
        } else {
            setLoan({...loan, [e.target.name]: value});
        }
    }

    const resetLoan = () => {
        setLoan(EMPTY_LOAN)
    }

    const handleBookChange = (e, index) => {
        const tempBooks = [...loan.loanedBooks];
        tempBooks[index] = e.target.value
        setLoan({...loan, loanedBooks: tempBooks})
    }

    const addBook = () => {
        setLoan({...loan, loanedBooks: [...loan.loanedBooks, {"isbn": ""}]})
    }

    const removeBook = (index) => {
        const tempBooks = [...loan.loanedBooks];
        tempBooks.splice(index, 1)
        setLoan({...loan, loanedBooks: tempBooks})
    }

    const handleSubmit = (e) => {
        if (new Date(loan.dueDate) < new Date(loan.dateOfLoan)) {
            alert("Date of lean cannot be later than due date.")
            return
        }
        e.preventDefault();
        confirmData(loan).then(() => {
            resetLoan()
        })
        console.log(loan);
    }


    // SIGN LOGIC

    class Callback {
        constructor() {
            this.onError = (e) => {
                console.error(e);
            };
        }
    }

    const handleSign = () => {
        console.log("SIGN")
        sign().then(r => console.log(r))
    }


    const sign = async () => {
        const xml = ""
        const xsdSchema = ""
        const xsltSchema = ""
        const pdfBase64 =  ""
        const ditec = (window).ditec;

        console.log("TESTER")

        // @ts-ignore
        ditec.dSigXadesJs.deploy(
            null,
            new Callback(function () {
                ditec.dSigXadesJs.initialize(
                    new Callback(function () {
                        ditec.dSigXadesJs.addXmlObject2(
                            "one",
                            "Reservation",
                            xml,
                            xsdSchema,
                            "http://gamesReservation",
                            "http://www.w3.org/2001/XMLSchema",
                            xsltSchema,
                            "https://www.w3.org/1999/XSL/Transform",
                            "HTML",
                            new Callback(function () {
                                ditec.dSigXadesJs.addPdfObject(
                                    "two",
                                    "ReservationPDF",
                                    pdfBase64?.split(`,`)[1],
                                    "",
                                    "http://objectFormatIdentifier",
                                    3,
                                    false,
                                    new Callback(function () {
                                        ditec.dSigXadesJs.sign11(
                                            "signatureId",
                                            "http://www.w3.org/2001/04/xmlenc#sha256",
                                            "urn:oid:1.3.158.36061701.1.2.3",
                                            "dataEnvelopeId",
                                            "http://dataEnvelopeURI",
                                            "dataEnvelopeDescr",
                                            new Callback(function () {
                                                ditec.dSigXadesJs.getSignedXmlWithEnvelope();
                                            })
                                        );
                                    })
                                );
                            })
                        );
                    })
                );
            })
        );
    };


    return (
        <main className="">
            <form className="" onSubmit={handleSubmit}>
                <h1>Loan a book</h1>

                <table>
                    <tbody>
                    <tr>
                        <td>Loan ID</td>
                        <td><input required pattern="[A-Z][0-9]{6}" type="text" name="loanId" value={loan.loanId}
                                   onChange={handleChange} placeholder="X123456"/></td>
                    </tr>
                    <tr>
                        <td>Librarian ID</td>
                        <td><input type="number" name="librarianId" value={loan.librarianId} onChange={handleChange}
                                   placeholder="1"/></td>
                    </tr>
                    <tr>
                        <td>Borrower Card Number</td>
                        <td><input required type="text" name="cardNumber" value={loan.borrower.cardNumber}
                                   onChange={(e) => handleChange(e, "borrower")}
                                   placeholder="ABC1DEF"/></td>
                    </tr>
                    <tr>
                        <td>Date Of Loan</td>
                        <td><input required type="date" name="dateOfLoan" value={loan.dateOfLoan}
                                   onChange={handleChange}
                                   placeholder="2023-01-01"/></td>
                    </tr>
                    <tr>
                        <td>Due Date</td>
                        <td><input required type="date" name="dueDate" value={loan.dueDate} onChange={handleChange}
                                   placeholder="2023-05-01"/></td>
                    </tr>
                    { (loan.dueDate!== "" && loan.dateOfLoan !== "") && loan.dueDate < loan.dateOfLoan &&
                        <tr>
                            <td></td>
                        <td style={{color: "red"}}> Date of lean cannot be later than due date.</td>
                        </tr>
                    }

                    <tr>
                        <td>
                            <h3>Books</h3>
                        </td>
                    </tr>
                    {loan.loanedBooks.map((book, index) => {
                        return <tr key={index}>
                            <td>ISBN</td>
                            <td>
                                <input required type="text" placeholder="0123456789"
                                       pattern="[0-9]{10}|[0-9]{13}"
                                       key={index}
                                       value={book.isbn}
                                       onChange={(e) => handleBookChange(e, index)}/>
                            </td>
                            <td>
                                {loan.loanedBooks.length > 1 &&
                                    <div onClick={() => removeBook(index)}>X</div>
                                }
                            </td>
                        </tr>
                    })}
                    </tbody>
                    <button type="button" onClick={addBook}>
                        +
                    </button>
                </table>
                <hr/>
                <button type="submit">
                    Submit data
                </button>
                <button type="button" onClick={() => validateData()}>
                    Validate data
                </button>
                <button type="button" onClick={() => transformData()}>
                    Transform data
                </button>
                <button type="button" onClick={handleSign}>
                    Sign data
                </button>
            </form>
        </main>
    )
        ;
}

export default App;
