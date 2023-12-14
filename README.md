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
# Usage {#usage}

### Tiles
To start, we define a **Tile** as any image with equal width and height. Set the **Tile Size** property according to the width and height of the images you plan to input:

<div style="width: 100%;display: grid;place-items: center;"><img alt="Set Tile Size" src="/readmeassets/setTileSize.png" /></div>
<br>


### Basic Tiles
Every single generation run requires that you provide a **Basic Tile,** think of this as a background image tile that will contrast with the rest of the tiles you define.

<div style="width: 100%;display: grid;place-items: center;"><img alt="Set Basic Tile" src="/readmeassets/setBasicTile.png" /></div>
<br>

For example, if we wanted to generate a map for a grassy biome, we might have our Basic Tile set to a grass tile like this*:

<p style="align: center;"><img alt="Set Tile Size" src="/readmeassets/grass0.png" /></p>
<br>

<p style="font-size: smaller;font-style: italic;">*32 pixel tile size</p>

### Border Tiles
The rest of the tiles you define will be **Border Tiles.** The images you input for this type of tile will be used to make fine-tuned, procedurally generated patches that properly border with the initially set Basic Tile.

Let's say you wanted to generate a very basic tile map that depicts a grass biome and has random patches of water randomly sized and spread throughout the map. A small section of this tile map may look something like this*:

<p style="align: center;"><img alt="Set Tile Size" src="/readmeassets/borderExample.png" /></p>
<br>

<p style="font-size: smaller;font-style: italic;">*Actual output generated by the service</p>

The Basic Tile used to generate the above image is the example shown in the **Basic Tiles** section above. As we can see, the Border Tiles we define will contrast with the set Basic Tile, so it is very important to set the Basic Tile according to the image you need.

Then we ask, what do we need at a minimum to produce a reusable, scalable tile (or set of tiles) which can simply be positioned correctly so that it contrasts (or borders) properly with the predefined Basic Tile? The answer is that we need at a minimum to define the following **4 subtypes**:

<p style="align: center;"><img alt="Set Tile Size" src="/readmeassets/markedBorderExample.png" /></p>
<br>

1. **Center Tile**
    A tile that represents the very center of our patch of bordering tile, the part of the Border Tile that **does not** touch the set Basic Tile on any side. In the above example, this could **only** be a tile that looks like this:

    <br>
        <div style="width: 100%;display: grid;place-items: center;">
            <img alt="Water Center Tile" src="/readmeassets/waterCenter0.png" />
        </div>
    </br>

    Use the Center Tile input to set the tile image you want to use for a tile that would appear in the very center of the Border Tile you're defining.

    <br>
        <div style="width: 100%;display: grid;place-items: center;">
            <img alt="Flat Border Select" src="/readmeassets/setCenterTile.png" />
        </div>
    </br>
2. **Flat Border**
    A tile that represents the portions of the generated patch that connect with the Basic Tile but isn't curved, the part of the Border Tile that borders the Basic Tile in a "straight" line only. In the above example, this could have been any one of the following **4 tile images**:

    <br>
        <p style="align: center;">
            <img alt="Water Flat Border 0" src="/readmeassets/waterFlat0.png" />
            <img alt="Water Flat Border 1" src="/readmeassets/waterFlat1.png" />
            <img alt="Water Flat Border 2" src="/readmeassets/waterFlat2.png" />
            <img alt="Water Flat Border 3" src="/readmeassets/waterFlat3.png" />
        </p>
    </br>

    YOU DO NOT NEED TO INPUT EVERY ONE OF THE THESE TILE IMAGES, you need only **input one of the above images** and it will be rotated to create an accordingly symmetrical tile image for the remaining three. Use the drop-down menu to select which **one** of the above valid forms of Flat Border tile images best corresponds to the image you plan to use for the Flat Border (See the [subtype reference][subtypeReferenceLink] to find which option represents which of the above forms).

    <br>
        <div style="width: 100%;display: grid;place-items: center;">
            <img alt="Flat Border Select" src="/readmeassets/setFlatBorder.png" />
        </div>
    </br>
3. **Outward Corner**
    A tile that represents the corner of the generated patch that curves outward and connects with the Basic Tile, the part of the Border Tile that borders the Basic Tile in a curve extending outward into the Basic Tile space. In the above example, this could have been any one of the following **4 tile images**:

    <br>
        <p style="align: center;">
            <img alt="Water Outward Corner 0" src="/readmeassets/waterOut0.png" />
            <img alt="Water Outward Corner 1" src="/readmeassets/waterOut1.png" />
            <img alt="Water Outward Corner 2" src="/readmeassets/waterOut2.png" />
            <img alt="Water Outward Corner 3" src="/readmeassets/waterOut3.png" />
        </p>
    </br>

    YOU DO NOT NEED TO INPUT EVERY ONE OF THE THESE TILE IMAGES, you need only **input one of the above images** and it will be rotated to create an accordingly symmetrical tile image for the remaining three. Use the drop-down menu to select which **one** of the above valid forms of Outward Corner tile images best corresponds to the image you plan to use for the Outward Corner (See the [subtype reference][subtypeReferenceLink] to find which option represents which of the above forms).

    <br>
        <div style="width: 100%;display: grid;place-items: center;">
            <img alt="Flat Border Select" src="/readmeassets/setOutwardCorner.png" />
        </div>
    </br>
4. **Inward Corner**
    A tile that represents the corner of the generated patch that curves inward and connects with the Basic Tile, the part of the Border Tile that borders the Basic TIle in a curve extending inward into the Border Tile space. In the above example, this could have been any one of the following **4 tile images**:

    <br>
        <p style="align: center;">
            <img alt="Water Inward Corner 0" src="/readmeassets/waterIn0.png" />
            <img alt="Water Inward Corner 1" src="/readmeassets/waterIn1.png" />
            <img alt="Water Inward Corner 2" src="/readmeassets/waterIn2.png" />
            <img alt="Water Inward Corner 3" src="/readmeassets/waterIn3.png" />
        </p>
    </br>

    YOU DO NOT NEED TO INPUT EVERY ONE OF THE THESE TILE IMAGES, you need only **input one of the above images** and it will be rotated to create an accordingly symmetrical tile image for the remaining three. Use the drop-down menu to select which **one** of the above valid forms of Inward Corner tile images best corresponds to the image you plan to use for the Inward Corner (See the [subtype reference][subtypeReferenceLink] to find which option represents which of the above forms).

    <br>
        <div style="width: 100%;display: grid;place-items: center;">
            <img alt="Flat Border Select" src="/readmeassets/setInwardCorner.png" />
        </div>
    </br>

## Border Tile Subtype Reference {#subtype}

### Straight Border Options
| Options | Bordered Left | Bordered Top | Bordered Right | Bordered Bottom |
|:----:|:----:|:----:|:----:|:----:|
| Flat Border | ![][wF0] | ![][wF1] | ![][wF2] | ![][wF3] |

### Curved Border Options
| Options | Top Left | Top Right | Bottom Right | Top Right |
|:----:|:----:|:----:|:----:|:----:|
| Outward Corner | ![][wO0] | ![][wO1] | ![][wO2] | ![][wO3] |
| Inward Corner | ![][wI0] | ![][wI1] | ![][wI2] | ![][wI3] |


---
# Deployment
The application is hosted on an **Amazon Web Services Elastic Compute** instance. 

### Core Features:

- Multi-container **Docker** application with separate containers for backend image processing and frontend user interface.
- Built-in **Java** implementation using [Wave Function Collapse][wfcLink] for efficient procedural generation.
- Containerization minimizes resource overhead, ensuring optimal performance.
- Currently deployed to a **Linux VM running on EC2.**


---
# Contributing
Pull requests are welcome. Please open a discussion post to talk about any large feature changes or updates you would like to see before making the pull request. Although the first production build is highly limited, the underlying framework was built with some extensibility options in mind and is compatible with a number of potential feature additions.

### To-Do List:
- [ ] **Allow user to set specific Border Tile subtypes**
    The current iteration of the service only allows entry of one of the subtype tile images for Border Tiles. However, for more **fine-tuned control of each tile image used** in the tile map generation process, the web application could be adapted to **allow users to specify up to all 4 images** of each of the Border Tile subtypes.
- [ ] **Decorations**
    The underlying image generation framework already has a working implementation for applying decorators to the output tile map image. **Decorators** are images that are applied on top of the tile map after generation, their primary purpose is for deblemishing the output of the generator and imposing non-tile images on top of the generated output (i.e. trees, buildings, boulders). The web application's interface could be updated to allow interactions which set Decorator images on the generated output (See the *[tiled-map-generator][tmgLink]* repository for an in-depth explanation on how Decorators are implemented).

[serviceLink]: http://34.168.145.3:3000/
[wfcLink]: https://www.youtube.com/watch?v=qRtrj6Pua2A
[usageLink]: #usage
[subtypeReferenceLink]: #subtype
[tmgLink]: https://github.com/ranjotdharni/tiled-map-generator
[apiFolderLink]: https://github.com/ranjotdharni/tmg-docker-app/tree/main/production/api
[orchFolderLink]: https://github.com/ranjotdharni/tmg-docker-app/blob/main/production/api/Orchestrator.java

[titleImage]: /readmeassets/title.png
[runExampleImage]: /readmeassets/runExample.png
[setTileSize]: /readmeassets/setTileSize.png

[wF0]: /readmeassets/waterFlat0.png
[wF1]: /readmeassets/waterFlat1.png
[wF2]: /readmeassets/waterFlat2.png
[wF3]: /readmeassets/waterFlat3.png

[wO0]: /readmeassets/waterOut0.png
[wO1]: /readmeassets/waterOut1.png
[wO2]: /readmeassets/waterOut2.png
[wO3]: /readmeassets/waterOut3.png

[wI0]: /readmeassets/waterIn0.png
[wI1]: /readmeassets/waterIn1.png
[wI2]: /readmeassets/waterIn2.png
[wI3]: /readmeassets/waterIn3.png

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