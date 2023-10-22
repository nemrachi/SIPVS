import axios from 'axios';
import switchDate from "./helpers.js";

// File for BE calls

export const validateData = async () => {
    try {
        const response = await axios.get('/api/zadanie1/validate');
        alert(response.data);
    } catch (error) {
        alert(error.response.data);
    }
};

export const transformData = async () => {
    try {
        const response = await axios.get('/api/zadanie1/transform');
        alert(response.data);
    } catch (error) {
        alert(error.response.data);
    }
};

export const signData = async () => {
   await sign
};

export const getXSD = async () => {
    try {
        const response = await axios.get('/api/zadanie2/getxsd');
        return response.data
    } catch (error) {
        console.log("XSD error", error)
        alert(error.response);
    }
};

export const getXSL = async () => {
    try {
        const response = await axios.get('/api/zadanie2/getxsl');
        return response.data;
    } catch (error) {
        console.log("XSL error", error)
        alert(error.response);
    }
};

export const getXML = async () => {
    try {
        const response = await axios.get('/api/zadanie2/getxml');
        console.log("XML Response",response)
        return response.data;
    } catch (error) {
        console.log("XLM error", error)
        alert(error.response);
    }
};

export const confirmData = async (loan) => {
    try {
        const data_to_send = { ...loan };
        data_to_send.dateOfLoan = switchDate(loan.dateOfLoan);
        data_to_send.dueDate = switchDate(loan.dueDate);
        data_to_send.librarianId = parseInt(loan.librarianId);
        data_to_send.loanedBooks = loan.loanedBooks.map((item) => {
            return { isbn: item };
        });

        const response = await axios.post('/api/zadanie1/save', data_to_send);
        alert(response.data);
        // UNCOMMENT TO DOWNLOAD FILE
        //
        // const blob = new Blob([response.data], {type: response.headers['content-type']});
        // const url = window.URL.createObjectURL(blob);
        //
        // const a = document.createElement('a');
        // a.href = url;
        // a.download = 'returned_file.txt';
        //
        // a.click();
        //
        // window.URL.revokeObjectURL(url);
    } catch (error) {
        alert(error.response.data);
    }
};
