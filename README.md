# Watcher
Watcher é um aplicativo que foi desenvolido para minha startup que utiliza diversos recursos do Google.
- API Google Maps: Para visualização do plantio.
- Google Firebase Database: Para persistencia dos dados (Ex: Localização, Temperatura, Umidade).
- Google Firebase Storage: Para armazenamento de arquivos (Ex: Fotos).
- Google Firebase Authentication: Para gerenciamento de acesso dos usuários.
- Google Clouse Vision: (Detecção de pragas por reconhecimento de imagem-).

## Funcionamento

Nossa Startup visa revolucionar a forma como é feita a amostragem e detecção de pragas e doenças nas plantações. Possui um Hardware 
autônomo (nosso produto), que funciona através de energia solar, comunicação wireless e utiliza conceitos de Internet das coisas e
Machine Learning. Coleta dados de Geolocalização, temperatura e umidade. Através da câmera, captura fotos
e faz o upload para o Google Cloud vision detectar pragas. Todos os dados coletados são disponibilizados no aplicativo.

- População no Mapa através da Latitude e Longitude disponibilizados pelo Watcher.
- Visualização de mapas de calor (HeatMap) através das temperaturas e umidades coletadas.
- Visualização da praga detectada pelo reconhecimento de imagem e local onde foi detectado.
- Biblioteca contendo descrição da praga e remédios de empresas parceiras indicados para tratamento.
