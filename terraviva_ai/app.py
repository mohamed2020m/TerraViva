import os
from fastapi import FastAPI, Form, UploadFile, File, HTTPException
from typing import List
from Obj3dTextGenerator import Obj3dTextGenerator
from QuizGeneratorPipeline import QuizGenerator, Question
# from helpers import extract_text_from_pdf, preprocess, save_to_tmp, split_text_into_chunks
from helpers import preprocess, save_to_tmp
from fastapi import FastAPI, UploadFile, File, HTTPException
from pathlib import Path
import logging
from fastapi_utils.tasks import repeat_every
import shutil
from dotenv import load_dotenv, find_dotenv
from fastapi.middleware.cors import CORSMiddleware

_ = load_dotenv(find_dotenv()) 

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

TMP_PATH = r"./tmp"

app = FastAPI()

app = FastAPI()
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # Allows all origins
    allow_credentials=True,
    allow_methods=["*"],  # Allows all HTTP methods (GET, POST, etc.)
    allow_headers=["*"],  # Allows all headers
)
quiz_generator = QuizGenerator()
obj3d_text_generator = Obj3dTextGenerator()

@app.on_event("startup")
@repeat_every(seconds=500) 
def cleanup_tmp_folder_task() -> None:
    tmp_folder = Path("tmp")
    if tmp_folder.exists() and tmp_folder.is_dir():
        shutil.rmtree(tmp_folder)
        logger.info("Temporary folder cleaned up.")   
           
@app.get("/")
async def entry():
    return {"message": "Terraviva AI APIs"}


@app.post("/generate-quiz", response_model=List[Question])
async def generate_quiz(
    file: UploadFile = File(...),
    num_questions: int = Form(10),
    model: str = Form("llama3-8b-8192")
):
    """Generate quiz questions from PDF content"""
    # try:
    # Read PDF file
    content = await file.read()
    
    # # Extract text from PDF
    # text = extract_text_from_pdf(content)
    
    # # Split into chunks
    # chunks = split_text_into_chunks(text)
    
    filename, file_extension = os.path.splitext(file.filename)
    
    # recreate tmp folder if it does not exist
    os.makedirs("tmp", exist_ok=True)
    
    # save the file to tmp folder
    file_name = save_to_tmp(filename, file_extension, content)
    logger.info(f"File saved to tmp folder: {file_name}")
    
    # Concatenate file_name and tmp_path with Path
    file_path = Path(TMP_PATH) / file_name
        
    # Preprocess the PDF content and retrieve relevant chunks
    chunks = preprocess(file_path)
    
    if not chunks:
        raise HTTPException(
            status_code=400,
            detail="Could not extract enough valid content from the PDF"
        )
        
    # Generate questions
    quiz_generator.set_model(model)
    questions = quiz_generator.generate_quiz(
        chunks=chunks, 
        num_questions=num_questions
    )
    print(questions)
    
    return questions
        
    # except Exception as e:
    #     raise HTTPException(status_code=400, detail=str(e))


@app.post("/object_3d-to-text")
async def obj3d_to_text(
    obj: UploadFile = File(...),
    model: str = Form("llama-3.2-11b-vision-preview")
):
    try:
        # Read file content
        content = await obj.read()
        
        filename, file_extension = os.path.splitext(obj.filename)
        
        # recreate tmp folder if it does not exist
        os.makedirs("tmp", exist_ok=True)
        
        # save the file to tmp folder
        file_name = save_to_tmp(filename, file_extension, content)
        logger.info(f"File saved to tmp folder: {file_name}")
        
        # Concatenate file_name and tmp_path with Path
        file_path = Path(TMP_PATH) / file_name
        
        # Generate description
        print(f"model name: {model}")
        obj3d_text_generator.set_model(model)
        description = await obj3d_text_generator.describe_object(file_path)

        # Return the response with the file path and description
        return {
            "path": str(file_path),
            "description": description
        }
        
    except Exception as e:
        raise HTTPException(
            status_code=400,
            detail=f"Error processing 3D object: {str(e)}"
        )

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("app:app", host="0.0.0.0", port=8000, reload=True)