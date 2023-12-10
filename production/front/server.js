const express = require('express');
const path = require('path');
const fetch = require('node-fetch');
const bodyParser = require('body-parser');

const app = express();
const PORT = process.env.PORT || 80;

// Serve static files from the 'dist' folder
app.use(bodyParser.json());
app.use(express.static(path.resolve('./dist')));

// Serve index.html for any other routes
app.post('/generate', async (req, res) => {
  try {
    const base64Image = req.body.base64Image; // Assuming you send the base64Image in the request body

    // Make a POST request to another API
    const apiResponse = await fetch('http://api:8000/upload', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/octet-stream',
      },
      body: base64Image, // Send the base64Image to the other API
      mode: 'no-cors'
    });

    // Check if the request to the other API was successful
    if (apiResponse.ok) {
      const result = await apiResponse.json(); // Handle the response from the other API
      res.status(200).json({ tileMap: result.message });
    } else {
      console.error('Image upload to another API failed: ', apiResponse.statusText);
      res.status(apiResponse.status).json({ error: 'Image upload to another API failed' });
    }
  } catch (error) {
    console.error('Error during image upload: ', error);
    res.status(500).json({ error: 'The server sent a 500 for internal error' });
  }
});

app.get('*', (req, res) => {
  res.sendFile(path.resolve('./dist/index.html'));
});

// Start the server
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});