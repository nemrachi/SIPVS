import React, { useState } from "react";
import {validateData, transformData, confirmData, uploadTimeStampFile, uploadDocsCheckFile} from "./api";

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
  const [selectedTimeStampFile, setSelectedTimeStampFile] = useState(null);
  const [selectedDocsCheckFile, setSelectedDocsCheckFile] = useState(null);
  const [checkedDocs, setCheckedDocs] = useState([]);

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

  const handleTimeStampFileChange = (event) => {
    setSelectedTimeStampFile(event.target.files[0]);
  };

  const handleTimeStampUpload = () => {
    if (selectedTimeStampFile) {
      const formData = new FormData();
      formData.append("file", selectedTimeStampFile);

      uploadTimeStampFile(formData).then((r) => console.log(r));
    } else {
      console.log("No file selected");
    }
  };

  const handleDocsCheckFileChange = (event) => {
    setSelectedDocsCheckFile(event.target.files[0]);
  };

  const handleDocsCheckUpload = () => {
    // TODO uncomment if uploading file is needed
      // if (selectedDocsCheckFile) {
          const formData = new FormData();
          // TODO uncomment if uploading file is needed
          // formData.append("file", selectedDocsCheckFile);

          uploadDocsCheckFile(formData).then((response) => {
            setCheckedDocs(response.data)
          });
      // } else {
      //     console.log("No file selected");
      // }
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
        <hr />
        <div>
          <input type="file" onChange={handleTimeStampFileChange} className="mb-1" />
          <button
            type="button"
            className="button-gray button-extra"
            onClick={handleTimeStampUpload}
          >
            Add timestamp
          </button>
        </div>
        <hr />
        <div>
          {/*TODO uncomment if uploading file is needed*/}
          {/*<input type="file" onChange={handleDocsCheckFileChange} className="mb-1" />*/}
          <button
              type="button"
              className="button-gray button-extra"
              onClick={handleDocsCheckUpload}
          >
            Verify the validity of documents
          </button>
        </div>
        {!!checkedDocs.length && <div>
          <h3>Checked documents</h3>
          {checkedDocs.map(doc => {
            console.log(doc)
            return <div className="document-status">
              <div className="document-status__header">
                <strong>{doc.filename}</strong>
                {!doc.errorMsg ? <strong className="color-green">OK</strong> : <strong className="color-red">FAIL</strong>}
              </div>
              <div className="document-status__body">
                <div>{doc.errorMsg}</div>
              </div>
              <hr/>
            </div>
          })}
        </div>}
      </form>
    </main>
  );
}

export default App;
