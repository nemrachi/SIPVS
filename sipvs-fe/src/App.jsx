import React, {useState} from 'react';
import axios from 'axios';

function App() {
    const [status, setStatus] = useState('');

    const confirmData = async () => {
        const data = {
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

        try {
            const response = await axios.post('/api/zadanie1/save', data, {
                responseType: 'blob'
            });
            console.log("Save endpoint response", response)
            setStatus("Downloaded");

            // Create a temporary URL for the blob
            const blob = new Blob([response.data], {type: response.headers['content-type']});
            const url = window.URL.createObjectURL(blob);

            // Create a link element to trigger the download
            const a = document.createElement('a');
            a.href = url;
            a.download = 'returned_file.txt'; // Set the desired file name and extension

            // Trigger a click event on the link to start the download
            a.click();

            // Trigger a click event on the link to start the download
            window.URL.revokeObjectURL(url);
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };

    return (
        <>
            <div className="container m-auto pt-20">
                <h1 className="text-4xl font-bold text-center">Požičovňa kníh</h1>
                <button onClick={confirmData} className="mt-4 bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                    Fetch Data
                </button>
                {status && <span className="ml-5">{status}</span>}
            </div>
        </>
    );
}

export default App;
