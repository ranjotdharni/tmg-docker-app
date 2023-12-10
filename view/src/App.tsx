import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import example from './assets/example.json'
import './App.css'
import './css/main.css'
import { ChangeEvent, MouseEvent, useState } from 'react'
import BorderTileInput from './components/BorderTileInput'

let FullBorderTile: [
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
  const [tileSize, setTileSize] = useState<number>(16)
  const [message, setMessage] = useState<string>('')
  const [download, setDownload] = useState<string>('')
  const [basicTile, setBasicTile] = useState<string>('')
  const [borderTiles, setBorderTiles] = useState<BorderTiles>([])

  function newMessage(message: string) {
    setMessage(message)
  }

  function handleSize(evt: ChangeEvent<HTMLInputElement>) {
    setTileSize(parseFloat(evt.target.value))
  }

  async function uploadImage(dataUrl: string) {
    try {
      const base64Image = dataUrl.split(',')[1]

      const response = await fetch(window.location.origin + '/generate', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ base64Image })
      });

      if (response.ok) {
        const result = await response.json()
        setDownload(result.tileMap) 
        newMessage("Complete")
      } 
      else {
        newMessage('Image upload failed: ' + response.statusText)
      }
    } 
    catch (error) {
      newMessage('Error during image upload: ' + error)
    }
  }

  async function handleSubmit(evt: MouseEvent<HTMLButtonElement>) {
    evt.preventDefault()

    setDownload('')

    if (tileSize < 4) {
      newMessage('Minimum tile size is 4 pixels')
      return
    }

    if (basicTile === '') {
      newMessage('Please Fill All Fields.')
      return
    } 

    (borderTiles as Array<typeof FullBorderTile>).forEach((borderTile: typeof FullBorderTile) => {
      if (borderTile[0].trim() === '') {
        newMessage('Please Fill All Fields.')
        return
      }

      borderTile[1].forEach((item) => {
        if (item === '')
        {
          newMessage('Please Fill All Fields.')
          return
        }
      })

      borderTile[2].forEach((item) => {
        if (item === '')
        {
          newMessage('Please Fill All Fields.')
          return
        }
      })

      borderTile[3].forEach((item) => {
        if (item === '')
        {
          newMessage('Please Fill All Fields.')
          return
        }
      })
    })

    newMessage("Generating...")

    const canvas = document.createElement('canvas')
    const context = canvas.getContext('2d')

    canvas.width = tileSize * 4
    canvas.height = tileSize + (tileSize * 4 * (borderTiles as Array<typeof FullBorderTile>).length)

    let img = new Image()
    img.src = basicTile

    context!.drawImage(img, 0, 0);

    (borderTiles as Array<typeof FullBorderTile>).forEach((borderTile: typeof FullBorderTile, tileIndex) => {
      let cursor: number = (tileIndex * tileSize * 4) + tileSize

      img.src = borderTile[0]
      context?.drawImage(img, 0, cursor)

      cursor = cursor + tileSize

      borderTile[1].forEach((item, idx) => {
        img.src = item
        context!.drawImage(img, idx * tileSize, cursor)
      })

      cursor = cursor + tileSize

      borderTile[2].forEach((item, idx) => {
        img.src = item
        context!.drawImage(img, idx * tileSize, cursor)
      })

      cursor = cursor + tileSize

      borderTile[3].forEach((item, idx) => {
        img.src = item
        context!.drawImage(img, idx * tileSize, cursor)
      })
    })

    await uploadImage(canvas.toDataURL())
  }

  async function runExample(evt: MouseEvent<HTMLButtonElement>) {
    evt.preventDefault()

    setDownload('')
    newMessage('Running example...')

    uploadImage(example.data)
  }

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

  function setBorderCenter(index: number): (borderCenter: string) => void {
    const updateBorderCenter = (borderCenter: string) => {
      const updatedArray: BorderTiles = [...borderTiles]
      const updatedItem: typeof FullBorderTile = [...borderTiles[index]]
      updatedItem[0] = borderCenter
      updatedArray[index] = updatedItem
      setBorderTiles(updatedArray)
    }

    return updateBorderCenter
  }

  function setBorderFlat(index: number): (borderFlat: [string, string, string, string]) => void {
    const updateBorderFlat = (borderFlat: [string, string, string, string]) => {
      const updatedArray: BorderTiles = [...borderTiles]
      const updatedItem: typeof FullBorderTile = [...borderTiles[index]]
      updatedItem[1] = borderFlat
      updatedArray[index] = updatedItem
      setBorderTiles(updatedArray)
    }

    return updateBorderFlat
  }

  function setBorderOut(index: number): (borderOut: [string, string, string, string]) => void {
    const updateBorderOut = (borderOut: [string, string, string, string]) => {
      const updatedArray: BorderTiles = [...borderTiles]
      const updatedItem: typeof FullBorderTile = [...borderTiles[index]]
      updatedItem[2] = borderOut
      updatedArray[index] = updatedItem
      setBorderTiles(updatedArray)
    }

    return updateBorderOut
  }

  function setBorderIn(index: number): (borderIn: [string, string, string, string]) => void {
    const updateBorderIn = (borderIn: [string, string, string, string]) => {
      const updatedArray: BorderTiles = [...borderTiles]
      const updatedItem: typeof FullBorderTile = [...borderTiles[index]]
      updatedItem[3] = borderIn
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
        (basicTile !== '' ? <img className='viewWidthTopBreak' src={basicTile} /> : <></>)
      }
      {
        (borderTiles as Array<typeof FullBorderTile>).map((item) => {
          return (
            <>
              {item[0] !== '' ? <img className='viewWidthTopBreak' src={item[0]} /> : <></>}
              {item[1].map((innerItem) => { return ( innerItem !== '' ? <img className='viewWidthTopBreak' src={innerItem} /> : <></> ) })}
              {item[2].map((innerItem) => { return ( innerItem !== '' ? <img className='viewWidthTopBreak' src={innerItem} /> : <></> ) })}
              {item[3].map((innerItem) => { return ( innerItem !== '' ? <img className='viewWidthTopBreak' src={innerItem} /> : <></> ) })}
            </>
          )
        })
      }

      {
        (
          download !== '' ?
          <><br></br><a className='result viewWidthTopBreak' href={download} download='result.png'><img src={download} alt='Click Here to Download' /></a></> :
          <></>
        )
      }

      <span className='setSize'>
        <label>Tile Size (pixels):</label>
        <input type='number' value={tileSize} onChange={handleSize} />
      </span>

      <button className='addBorder vertical frontButton' onClick={addBorderTile}>Add Border Tile</button>
      <button className='generate vertical frontButton' onClick={handleSubmit}>Generate</button>
      <br></br>
      <button className='example frontButton' onClick={runExample}>Run Example</button>
      <button className='instructions frontButton' onClick={() => {window.open('https://github.com/ranjotdharni/tmg-docker-app', '_blank');}}>Instructions</button>
      <p className='message'>{message}</p>

      <div className='creditBox'>
        <h2>Powered By</h2>
        <a href="https://vitejs.dev" target="_blank">
          <img src={viteLogo} className="logo" alt="Vite logo" />
        </a>
        <a href="https://react.dev" target="_blank">
          <img src={reactLogo} className="logo react" alt="React logo" />
        </a>
      </div>
        
      <form className='formStyle'>
        <span>
          <label>Basic Tile</label>
          <br></br>
          <input type='file' onChange={handleUpload} />
        </span>

        {
          (borderTiles as Array<typeof FullBorderTile>).map((bTile, index) => {
            return <BorderTileInput 
              key={bTile[4]} 
              removePassUp={removeBorderTile(index)}
              centerPassUp={setBorderCenter(index)} 
              flatPassUp={setBorderFlat(index)}
              outwardPassUp={setBorderOut(index)}
              inwardPassUp={setBorderIn(index)}
            />
          })
        }
      </form>
    </>
  )
}

export default App
