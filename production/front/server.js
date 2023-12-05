const express = require('express');
const path = require('path');
const fetch = require('node-fetch');

const app = express();
const PORT = process.env.PORT || 80;

// Serve static files from the 'dist' folder
app.use(express.static(path.resolve('./dist')));

// Serve index.html for any other routes
app.get('/hello', async (req, res) => {
  try {
    const response = await fetch('http://api:8000/hello', { method: 'GET', mode: 'no-cors' });
    const data = await response.text();
    console.log(data);
    //const data = await response.json();
    res.json({response: data});
  } catch (error) {
    console.error('Fetch error: ', error);
    res.status(500).send('ISE Happened');
  }
});

app.get('*', (req, res) => {
  res.sendFile(path.resolve('./dist/index.html'));
});

// Start the server
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});