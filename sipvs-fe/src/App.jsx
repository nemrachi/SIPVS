import React, { useState } from "react";
import {
  validateData,
  transformData,
  confirmData,
  uploadFile,
} from "./api";

import SignComponent from "./SignComponent";

function App() {
  const EMPTY_LOAN = {
    loanId: "",
    librarianId: 0,
    borrower: {
      cardNumber: "",
    },
    dateOfLoan: "",
    dueDate: "",
    loanedBooks: [{ isbn: "" }],
  };
  const [loan, setLoan] = useState(EMPTY_LOAN);
  const [selectedFile, setSelectedFile] = useState(null);

  const handleChange = (e, nested) => {
    const value = e.target.value;
    if (nested) {
      setLoan({
        ...loan,
        [nested]: { ...loan[nested], [e.target.name]: value },
      });
    } else {
      setLoan({ ...loan, [e.target.name]: value });
    }
  };

  const resetLoan = () => {
    setLoan(EMPTY_LOAN);
  };

  const handleBookChange = (e, index) => {
    const tempBooks = [...loan.loanedBooks];
    tempBooks[index] = e.target.value;
    setLoan({ ...loan, loanedBooks: tempBooks });
  };

  const addBook = () => {
    setLoan({ ...loan, loanedBooks: [...loan.loanedBooks, { isbn: "" }] });
  };

  const removeBook = (index) => {
    const tempBooks = [...loan.loanedBooks];
    tempBooks.splice(index, 1);
    setLoan({ ...loan, loanedBooks: tempBooks });
  };

  const handleSubmit = (e) => {
    if (new Date(loan.dueDate) < new Date(loan.dateOfLoan)) {
      alert("Date of lean cannot be later than due date.");
      return;
    }
    e.preventDefault();
    confirmData(loan).then(() => {
      resetLoan();
    });
    console.log(loan);
  };

  const handleFileChange = (event) => {
    setSelectedFile(event.target.files[0]);
  };

  const handleUpload = () => {
    if (selectedFile) {
      const formData = new FormData();
      formData.append("file", selectedFile);

      uploadFile(formData).then((r) => console.log(r));
    } else {
      console.log("No file selected");
    }
  };

  const handleSign = () => {
    SignComponent().then((r) => console.log(r));
  };

  const isInputInvalid = (pattern, value) => {
    if (value) {
      return !pattern.test(value) && value.length > 1;
    }
  };
  return (
    <main className="">
      <form className="" onSubmit={handleSubmit}>
        <h1>Loan a book</h1>
        <table>
          <tbody>
            <tr>
              <td>Loan ID</td>
              <td>
                <input
                  required
                  pattern="[A-Z][0-9]{6}"
                  type="text"
                  name="loanId"
                  value={loan.loanId}
                  onChange={handleChange}
                  placeholder="X123456"
                />
                {isInputInvalid(/^[A-Z][0-9]{6}$/, loan.loanId) && (
                  <td className="error-message">
                    Loan ID is not in correct format
                  </td>
                )}
              </td>
            </tr>
            <tr>
              <td>Librarian ID</td>
              <td>
                <input
                  type="number"
                  min="0"
                  name="librarianId"
                  value={loan.librarianId}
                  onChange={handleChange}
                  placeholder="1"
                />
              </td>
            </tr>
            <tr>
              <td>Borrower Card Number</td>
              <td>
                <input
                  required
                  type="text"
                  pattern="[A-Z0-9]{7}"
                  name="cardNumber"
                  value={loan.borrower.cardNumber}
                  onChange={(e) => handleChange(e, "borrower")}
                  placeholder="ABC1DEF"
                />
                {isInputInvalid(/^[A-Z0-9]{7}$/, loan.borrower.cardNumber) && (
                  <td className="error-message">
                    Borrower card number is not in correct format
                  </td>
                )}
              </td>
            </tr>
            <tr>
              <td>Date Of Loan</td>
              <td>
                <input
                  required
                  type="date"
                  name="dateOfLoan"
                  value={loan.dateOfLoan}
                  onChange={handleChange}
                  placeholder="2023-01-01"
                />
              </td>
            </tr>
            <tr>
              <td>Due Date</td>
              <td>
                <input
                  required
                  type="date"
                  name="dueDate"
                  value={loan.dueDate}
                  onChange={handleChange}
                  placeholder="2023-05-01"
                />
              </td>
            </tr>
            {loan.dueDate !== "" &&
              loan.dateOfLoan !== "" &&
              loan.dueDate < loan.dateOfLoan && (
                <tr>
                  <td></td>
                  <td style={{ color: "red" }}>
                    {" "}
                    Date of lean cannot be later than due date.
                  </td>
                </tr>
              )}

            <tr>
              <td>
                <h3>Books</h3>
              </td>
            </tr>
            {loan.loanedBooks.map((book, index) => {
              return (
                <tr key={index}>
                  <td>ISBN</td>
                  <td>
                    <input
                      required
                      type="text"
                      placeholder="0123456789"
                      pattern="[0-9]{10}|[0-9]{13}"
                      key={index}
                      value={book.isbn}
                      onChange={(e) => handleBookChange(e, index)}
                    />
                    {isInputInvalid(/^[0-9]{10}|[0-9]{13}$/, book) && (
                      <td className="error-message">
                        ISBN is not in correct format
                      </td>
                    )}
                  </td>
                  <td>
                    {loan.loanedBooks.length > 1 && (
                      <button onClick={() => removeBook(index)}>X</button>
                    )}
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
        <button
          type="button"
          className="button-gray button-extra"
          onClick={addBook}
        >
          Add book +
        </button>
        <hr />
        <button className="button-gray" type="submit">
          Submit data
        </button>
        <button
          className="button-gray"
          type="button"
          onClick={() => validateData()}
        >
          Validate data
        </button>
        <button
          className="button-gray"
          type="button"
          onClick={() => transformData()}
        >
          Transform data
        </button>
        <button className="button-gray" type="button" onClick={handleSign}>
          Sign data
        </button>
      </form>
      <div>
          <input type="file" onChange={handleFileChange} />
          <button className="button-gray" onClick={handleUpload}>
            Upload
          </button>
        </div>
    </main>
  );
}

export default App;
