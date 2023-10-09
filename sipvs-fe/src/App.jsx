import React, {useState} from 'react';
import axios from 'axios';

function App() {
    const [wizard, setWizard] = useState(false);

    const EMPTY_LOAN = {
        loanId: "",
        librarianId: "",
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
        if (value === "magic") {
            doMagic()
        } else if (value === "mukel") {
            setWizard(false)
        }
    }

    const resetLoan = () => {
        setLoan(EMPTY_LOAN)
    }

    const doMagic = () => {
        setWizard(true)
        setLoan({
            "loanId": "L000002",
            "librarianId": 2,
            "borrower": {
                "cardNumber": "ABCDEFG"
            },
            "dateOfLoan": "2023-10-05",
            "dueDate": "2023-11-05",
            "loanedBooks": [
                {
                    "isbn": "0123456789"
                },
                {
                    "isbn": "0123456789012"
                }
            ]
        })
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
        e.preventDefault();
        confirmData().then(() => {
            resetLoan()
        })
        console.log(loan);
    }

    const confirmData = async () => {
        try {
            const response = await axios.post('/api/zadanie1/save', loan);
            alert(response.data)
            console.log("Save endpoint response", response)

            // UNCOMMENT TO DOWNLOAD FILE
            //
            // // Create a temporary URL for the blob
            // const blob = new Blob([response.data], {type: response.headers['content-type']});
            // const url = window.URL.createObjectURL(blob);
            //
            // // Create a link element to trigger the download
            // const a = document.createElement('a');
            // a.href = url;
            // a.download = 'returned_file.txt'; // Set the desired file name and extension
            //
            // // Trigger a click event on the link to start the download
            // a.click();
            //
            // // Trigger a click event on the link to start the download
            // window.URL.revokeObjectURL(url);
        } catch (error) {
            alert(error.response.data)
        }
    };
    const validateData = async () => {
        try {
            const response = await axios.get('/api/zadanie1/validate');
            alert(response.data)

        } catch (error) {
            alert(error.response.data)
        }
    };

    const transformData = async () => {
        try {
            const response = await axios.get('/api/zadanie1/transform');
            alert(response.data)

        } catch (error) {
            alert(error.response.data)
        }
    };

    return (
        <main className="">
            <form className="" onSubmit={handleSubmit} style={{
                display: "block",
                maxWidth: "min-content",
                textAlign: "center",
                marginLeft: "auto",
                marginRight: "auto",
                marginTop: "7%",
                padding: "25px",
                borderRadius: "7px",
                boxShadow: "1px 7px 13px 7px lightgrey"
            }}>
                <h1>Loan a book{wizard && "🧙‍♂️"}</h1>

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
                    <tr>
                        <td>
                            <h3>Knihy</h3>
                        </td>
                    </tr>
                    {loan.loanedBooks.map((book, index) => {
                        return <tr>
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
                {/*{loan.loanedBooks.map((book, index) => {*/}
                {/*    return <label>*/}
                {/*        ISBN of Book:*/}
                {/*        <div className="flex gap-2 pr-1 leading-[2.2]">*/}

                {/*            }*/}
                {/*        </div>*/}
                {/*    </label>*/}
                {/*})}*/}
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
                {wizard && <button type="button"
                                   onClick={() => alert("Crying... 😓🥺😭😭")}>
                    😭 Cry
                </button>}
            </form>
        </main>
    )
        ;
}

export default App;
