import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import './css/main.css'
import { ChangeEvent, MouseEvent, useState } from 'react'
import BorderTileInput from './components/BorderTileInput'

let FullBorderTile: [
  string, //id
  string, //center tile
  [string, string, string, string], // flat borders
  [string, string, string, string], // outward corner borders
  [string, string, string, string], // inward corner boders
  string //key
]

interface BorderTiles {
  [index: number]: typeof FullBorderTile,
  [Symbol.iterator](): IterableIterator<typeof FullBorderTile>
}

function App() {
  const [basicTile, setBasicTile] = useState<string>()
  const [borderTiles, setBorderTiles] = useState<BorderTiles>([])

  function handleUpload(evt: ChangeEvent<HTMLInputElement>) {
    const file = evt.target.files![0]

    if (file != undefined) {
        const reader = new FileReader()
        
        reader.onload = (evt) => {
            const img = new Image()
            img.src = evt.target!.result as string

            img.onload = () => {
                setBasicTile(img.src)
            }
        }

        reader.readAsDataURL(file)
    }
  }

  function setBorderId(index: number): (borderId: string) => void {
    const updateBorderId = (borderId: string) => {
      const updatedArray: BorderTiles = [...borderTiles]
      const updatedItem: typeof FullBorderTile = [...borderTiles[index]]
      updatedItem[0] = borderId
      updatedArray[index] = updatedItem
      setBorderTiles(updatedArray)
    }

    return updateBorderId
  }

  function setBorderCenter(index: number): (borderCenter: string) => void {
    const updateBorderCenter = (borderCenter: string) => {
      const updatedArray: BorderTiles = [...borderTiles]
      const updatedItem: typeof FullBorderTile = [...borderTiles[index]]
      updatedItem[1] = borderCenter
      updatedArray[index] = updatedItem
      setBorderTiles(updatedArray)
    }

    return updateBorderCenter
  }

  function setBorderFlat(index: number): (borderFlat: [string, string, string, string]) => void {
    const updateBorderFlat = (borderFlat: [string, string, string, string]) => {
      const updatedArray: BorderTiles = [...borderTiles]
      const updatedItem: typeof FullBorderTile = [...borderTiles[index]]
      updatedItem[2] = borderFlat
      updatedArray[index] = updatedItem
      setBorderTiles(updatedArray)
    }

    return updateBorderFlat
  }

  function setBorderOut(index: number): (borderOut: [string, string, string, string]) => void {
    const updateBorderOut = (borderOut: [string, string, string, string]) => {
      const updatedArray: BorderTiles = [...borderTiles]
      const updatedItem: typeof FullBorderTile = [...borderTiles[index]]
      updatedItem[3] = borderOut
      updatedArray[index] = updatedItem
      setBorderTiles(updatedArray)
    }

    return updateBorderOut
  }

  function setBorderIn(index: number): (borderIn: [string, string, string, string]) => void {
    const updateBorderIn = (borderIn: [string, string, string, string]) => {
      const updatedArray: BorderTiles = [...borderTiles]
      const updatedItem: typeof FullBorderTile = [...borderTiles[index]]
      updatedItem[4] = borderIn
      updatedArray[index] = updatedItem
      setBorderTiles(updatedArray)
    }

    return updateBorderIn
  }

  function removeBorderTile(index: number): () => void {
    const updateBorderTileArray = () => {
      let updatedArray = [...borderTiles]
      updatedArray = updatedArray.filter((_, i) => i !== index)
      setBorderTiles(updatedArray)
    }

    return updateBorderTileArray
  }

  function addBorderTile(evt: MouseEvent<HTMLButtonElement>) {
    evt.preventDefault()

    const updatedArray = [...borderTiles]
    updatedArray.push([
      '', 
      '', 
      ['', '', '', ''], 
      ['', '', '', ''], 
      ['', '', '', ''],
      'border' + Math.floor(Math.random() * 1000000)
    ])

    setBorderTiles(updatedArray)
  }

  return (
    <>
      {
        (basicTile !== '' ? <img src={basicTile} /> : <></>)
      }
      {
        (borderTiles as Array<typeof FullBorderTile>).map((item, index) => {
          return (
            <>
              {item[1] !== '' ? <img src={item[1]} /> : <></>}
              {item[2].map((innerItem, idx) => { return ( innerItem !== '' ? <img src={innerItem} /> : <></> ) })}
              {item[3].map((innerItem, idx) => { return ( innerItem !== '' ? <img src={innerItem} /> : <></> ) })}
              {item[4].map((innerItem, idx) => { return ( innerItem !== '' ? <img src={innerItem} /> : <></> ) })}
            </>
          )
        })
      }

      <form className='formStyle'>
        <span>
          <label>Basic Tile</label>
          <br></br>
          <input type='file' onChange={handleUpload} />
        </span>

        {
          (borderTiles as Array<typeof FullBorderTile>).map((bTile, index) => {
            return <BorderTileInput 
              key={bTile[5]} 
              removePassUp={removeBorderTile(index)}
              idPassUp={setBorderId(index)} 
              centerPassUp={setBorderCenter(index)} 
              flatPassUp={setBorderFlat(index)}
              outwardPassUp={setBorderOut(index)}
              inwardPassUp={setBorderIn(index)}
            />
          })
        }
      </form>

      <button className='addBorder' onClick={addBorderTile}>Add Border Tile</button>

      <div className='creditBox'>
        <h2>Powered By</h2>
        <a href="https://vitejs.dev" target="_blank">
          <img src={viteLogo} className="logo" alt="Vite logo" />
        </a>
        <a href="https://react.dev" target="_blank">
          <img src={reactLogo} className="logo react" alt="React logo" />
        </a>
      </div>
        
    </>
  )
}

export default App
