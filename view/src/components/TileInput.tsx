import { useState, ChangeEvent, MouseEvent } from 'react'
import '../css/tileInput.css'

interface props {
    tileTypes: [string, string, string, string],
    passUp: (tiles: [string, string, string, string]) => void
}

function SingleTileInput({ tileTypes, passUp } : { tileTypes: [string, string, string, string], passUp: (data: [string, string, string, string]) => void }) {
    const [type, setType] = useState<number>(0)

    function getRotatedImage(image: HTMLImageElement, rotationIndex: number): string {
        const canvas = document.createElement('canvas')
        const context = canvas.getContext('2d')

        canvas.width = image.width
        canvas.height = image.height

        context!.rotate(rotationIndex * Math.PI / 2)
        context!.drawImage(image, (rotationIndex < 2 ? 0 : -image.width), (rotationIndex < 1 || rotationIndex > 2 ? 0 : -image.height))

        return canvas.toDataURL()
    }

    function handleUpload(evt: ChangeEvent<HTMLInputElement>) {
        const file = evt.target.files![0]

        if (file != undefined) {
            const reader = new FileReader()
            
            reader.onload = (evt) => {
                const img = new Image()
                img.src = evt.target!.result as string

                img.onload = () => {
                    let itr: number = 0
                    let index: number = type
                    let tiles: [string, string, string, string] = ['', '', '', '']

                    while (itr < 4) {
                        if (index === 4)
                        {
                            index = 0
                        }

                        tiles[index] = getRotatedImage(img, itr)

                        index = index + 1
                        itr = itr + 1
                    }

                    passUp(tiles)
                }
            }

            reader.readAsDataURL(file)
        }
    }

    function handleChange(evt: ChangeEvent<HTMLSelectElement>) {
        setType(Number(evt.target.value))
    }

    return (
        <div>
            <select value={type} onChange={handleChange}>
                {
                    tileTypes.map((value, index) => {
                        return (
                            <option value={index}>{value}</option>
                        )
                    })
                }
            </select>
            <input type='file' onChange={handleUpload} />
        </div>
    )
}

export default function TileInput(props: props) {

    return (
        <SingleTileInput tileTypes={props.tileTypes} passUp={props.passUp} />
    )
}