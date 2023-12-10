import { ChangeEvent } from "react";
import TileInput from "./TileInput";

let borderPassUp: (images: [string, string, string, string]) => void

interface props {
    centerPassUp: (images: string) => void,
    flatPassUp: typeof borderPassUp,
    outwardPassUp: typeof borderPassUp,
    inwardPassUp: typeof borderPassUp,
    removePassUp: () => void
}

export default function BorderTileInput(props: props) {

    function handleUpload(evt: ChangeEvent<HTMLInputElement>) {
        const file = evt.target.files![0]

        if (file != undefined) {
            const reader = new FileReader()
            
            reader.onload = (evt) => {
                const img = new Image()
                img.src = evt.target!.result as string

                img.onload = () => {
                    props.centerPassUp(img.src)
                }
            }

            reader.readAsDataURL(file)
        }
    }


    return (
        <div style={{border: 'solid 1px palevioletred', padding: '5px', marginBottom: '10px', borderRadius: '1em'}}>
            <label>Center Tile</label>
            <div>
                <input type='file' onChange={handleUpload} />
            </div>

            <label>Flat Border</label>
            <TileInput passUp={props.flatPassUp} tileTypes={['Bordered Left', 'Bordered Top', 'Bordered Right', 'Bordered Bottom']} />

            <label>Outward Corner</label>
            <TileInput passUp={props.outwardPassUp} tileTypes={['Top Left', 'Top Right', 'Bottom Right', 'Bottom Left']} />

            <label>Inward Corner</label>
            <TileInput passUp={props.inwardPassUp} tileTypes={['Top Left', 'Top Right', 'Bottom Right', 'Bottom Left']} />

            <button onClick={() => {props.removePassUp()}}>Remove</button>
        </div>
    )
}