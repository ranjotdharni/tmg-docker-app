![Tile Map Generator][titleImage]
# Overview
Welcome to the Tile Map Generation repository!

This repository houses a powerful procedural generation application designed to create *1920x1080* images of a **2D tile map** from a set of basic tile images. Whether you're a developer or a user interested in **generating unique visual content,** this application provides a seamless experience.

Explore the [instructions][usageLink] for proper usage guidelines and essential terminology. Dive into the tools provided by this robust application and start generating captivating 2D tile maps effortlessly. You can access the service [here][serviceLink].

---
# Development

This application was built using the following **Technologies**:

- [![React][reactImgUrl]][reactUrl]
- [![Docker][dockerImgUrl]][dockerUrl]
- [![Java][javaImgUrl]][javaUrl]
- [![Node][nodeImgUrl]][nodeUrl]
- [![TypeScript][typescriptImgUrl]][typescriptUrl]

### Frontend Interface
- A simple **Express** server acts as a minimal proxy for requests to GET the web page.
- The service will produce an example tile map when you click the ***Run Example* button** which generates a very basic output that uses preloaded, 32px tiles: <br>
![Run Example Image][runExampleImage]
</br>
- Input tile images will be flattened into a single spritesheet before transmission to the image generation service.

### Backend Generator
The image generator (built in Java) was made first, then the web application was developed based on that design to allow for a user friendly method of interacting with the generator. A development guide and full-feature breakdown of the original image generator is available in the repository *[tiled-map-generator][tmgLink]*. The image generator implementation found in [/production/api][apiFolderLink] is built on top of the code found in the *tiled-map-generator* repository.

The primary differences between the original implementation and the iteration used in production here **observe different methods for loading resources.** The original source code employs a file-based system for routing the algorithm's output to the input tile images. The deployed iteration relies on the generator providing the corresponding tile coordinates for routing the algorithm's output to the input tiles images from a single spritesheet containing all possible tiles. As a result, we also observe the [Orchestrator][orchFolderLink] class replacing the Generator class and the omission of the Sprite class, both of which primarily served as tools during the development stages.

**The Orchestrator class simply initializes resources and coordinates the execution of the generation algorithm,** otherwise it works almost identically to the Generator class under the hood. The update from the original class was simply to instate **a centralized, additive solution for efficiently managing the multi-step process of reading and handling input** from the frontend.

---
# Usage



---
# Deployment
The application is hosted on a **Google Cloud Compute Engine** instance. 

### Core Features:

- Multi-container **Docker** application with separate containers for backend processing and frontend user interface.
- Built-in **Java** implementation using [Wave Function Collapse][wfcLink] for efficient procedural generation.
- Containerization minimizes resource overhead, ensuring optimal performance.
- Currently deployed to a **Linux VM running on Compute Engine**, with plans for migration to AWS EC2 in the near future for improved maintainability (cost considerations).

[serviceLink]: http://34.168.145.3:3000/
[wfcLink]: https://www.youtube.com/watch?v=qRtrj6Pua2A
[usageLink]: https://github.com/ranjotdharni/tmg-docker-app#Usage
[tmgLink]: https://github.com/ranjotdharni/tiled-map-generator
[apiFolderLink]: https://github.com/ranjotdharni/tmg-docker-app/tree/main/production/api
[orchFolderLink]: https://github.com/ranjotdharni/tmg-docker-app/blob/main/production/api/Orchestrator.java

[titleImage]: /readmeassets/title.png
[runExampleImage]: /readmeassets/runExample.png

[reactImgUrl]: https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=1c2c4c
[reactUrl]: https://react.dev/
[dockerImgUrl]: https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white
[dockerUrl]: https://www.docker.com/
[javaImgUrl]: https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=black
[javaUrl]: https://www.java.com/en/
[nodeImgUrl]: https://img.shields.io/badge/Node-339933?style=for-the-badge&logo=node.js&logoColor=darkgreen
[nodeUrl]: https://nodejs.org/en
[typescriptImgUrl]: https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=typescript&logoColor=white
[typescriptUrl]: https://www.typescriptlang.org/