from fastapi import FastAPI, HTTPException
from fastapi.responses import JSONResponse
from pydantic import BaseModel, Field

from chatbot import criar_contexto_cliente, perguntar


app = FastAPI(
    title="Chatbot da Loja Virtual",
    description="API da assistente virtual Íris.",
    version="1.0.0",
)


class ChatBotPergunta(BaseModel):
    nome: str = Field(..., min_length=1)
    cpf: str = Field(..., min_length=1)
    email: str = Field(..., min_length=1)
    pergunta: str = Field(..., min_length=1)

    def model_post_init(self, __context):
        self.nome = self.nome.strip()
        self.cpf = self.cpf.strip()
        self.email = self.email.strip()
        self.pergunta = self.pergunta.strip()

        if not self.cpf:
            raise ValueError("O CPF não pode estar vazio.")

        if not self.email:
            raise ValueError("O e-mail não pode estar vazio.")

        if not self.pergunta:
            raise ValueError("A pergunta não pode estar vazia.")


@app.get("/")
def inicio():
    return {
        "mensagem": "O chat está funcionando!"
    }


@app.get("/health")
def health():
    return {
        "status": "ok"
    }


@app.post("/chat")
def conversar(dados: ChatBotPergunta):
    try:
        contexto = criar_contexto_cliente(
            dados.cpf,
            dados.email,
        )

        resposta = perguntar(
            dados.pergunta,
            contexto,
        )

        return JSONResponse(
            content={"resposta": resposta},
            headers={"Content-Type": "application/json; charset=utf-8"},
        )

    except ValueError as exc:
        raise HTTPException(
            status_code=400,
            detail=str(exc),
        ) from exc

    except RuntimeError as exc:
        raise HTTPException(
            status_code=500,
            detail=str(exc),
        ) from exc

    except Exception as exc:
        print(
            f"[ERRO API] {type(exc).__name__}: {exc}"
        )
        raise HTTPException(
            status_code=500,
            detail=(
                f"{type(exc).__name__}: {exc}"
            ),
        ) from exc
