import React, {useState} from 'react';
import axios from 'axios';

function App() {
    const [data, setData] = useState(null);

    const fetchData = async () => {
        try {
            const response = await axios.post('/api/zadanie1/save', {
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
            });
            setData(response.data);
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };

    return (
        <>
            <div className="container m-auto pt-20">
                <h1 className="text-4xl font-bold text-center">Požičovňa kníh</h1>
                <button onClick={fetchData} className="mt-4 bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                    Fetch Data
                </button>
                {data && (
                    <div className="mt-4">
                        <h2>Data from API:</h2>
                        <pre>{JSON.stringify(data, null, 2)}</pre>
                    </div>
                )}
            </div>
        </>
    );
}

export default App;
