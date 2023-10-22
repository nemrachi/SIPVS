// File for helper functions
function switchDate(timestamp) {
    let date = new Date(timestamp);
    let year = date.getFullYear();
    let month = ("0" + (date.getMonth() + 1)).slice(-2); // Add leading 0 if necessary
    let day = ("0" + date.getDate()).slice(-2); // Add leading 0 if necessary

    let newTimestamp = `${year}-${month}-${day}`;

    return newTimestamp;
}

export default switchDate