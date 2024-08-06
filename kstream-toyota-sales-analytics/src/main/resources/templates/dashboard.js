// dashboard.js

// Function to handle incoming messages from the server
function handleServerEvent(event) {
    console.log('Received data:', event.data); // Debugging line

    try {
        const data = JSON.parse(event.data);
        console.log('Parsed data:', data); // Debugging line

        // Check if data is an object
        if (typeof data !== 'object' || data === null) {
            console.error('Data is not an object:', data);
            return;
        }

        const tableBody = document.getElementById('stateData');
        let row = Array.from(tableBody.rows).find(row => row.cells[0].textContent === data.stateName);

        if (row) {
            // Highlight the row first
            row.classList.add('highlight');

            // Update the count value after a short delay
            setTimeout(() => {
                row.cells[1].textContent = data.count;

                // Remove the highlight after updating the count
                setTimeout(() => {
                    row.classList.remove('highlight');
                }, 2000); // Keep highlight for 2 seconds
            }, 200); // Delay count update slightly for better visual effect
        } else {
            // Create a new row for the new state
            row = document.createElement('tr');
            const stateNameCell = document.createElement('td');
            stateNameCell.textContent = data.stateName;
            stateNameCell.classList.add('stateName');

            const countCell = document.createElement('td');
            countCell.textContent = data.count;
            countCell.classList.add('count');

            row.appendChild(stateNameCell);
            row.appendChild(countCell);

            tableBody.appendChild(row);

            // Highlight the new row
            row.classList.add('highlight');

            // Remove the highlight after 2 seconds
            setTimeout(() => {
                row.classList.remove('highlight');
            }, 2000);
        }

        // Limit the number of rows displayed
        while (tableBody.rows.length > 10) { // Adjust this number based on screen space
            tableBody.deleteRow(0); // Remove the oldest row
        }

    } catch (error) {
        console.error('Error parsing data:', error);
    }
}

// Initialize EventSource to listen for updates from the server
function initializeEventSource() {
    const eventSource = new EventSource('/reactive/stream/car/sales');

    eventSource.onmessage = handleServerEvent;

    eventSource.onerror = function (error) {
        console.error('EventSource failed:', error);
        // Optionally handle errors or reconnect
    };
}

// Start listening for server events
initializeEventSource();
