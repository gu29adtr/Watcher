# Watcher
Watcher é um aplicativo que foi desenvolido usando diversos recursos do Google
- API Google Maps: Para visualização
- Google Firebase Database: Para persistencia dos dados (Ex: Localização, Temperatura, Umidade)
- Google Firebase Storage: Para armazenamento de arquivos (Ex: Fotos)
- Google Firebase Authentication: Para gerenciamento de acesso dos usuários
- Google Clouse Vision: (Hardware)

## Funcionamento

Nossa Startup possui um Hardware autônomo (utilização no agronegócio para detecar pragas e doenças), que funciona através de energia solar,
comunicação wireless e utiliza conceitos de Internet das coisas e Machine Learning. Coleta dados de Geolocalização, Temperatura, umidade
e os envia para o Database. E através de uma câmera, captura fotos e faz um reconhecimento de imagem para detectar pragas no plantio.
- População no Mapa através da Latitude e Longitude disponibilizados pelo Watcher.
- Visualização de mapas de calor (HeatMap) através das temperaturas e umidades coletadas.
- Visualização da praga detectada pelo reconhecimento de imagem e local onde foi detectado.
- Biblioteca contendo descrição da praga e remédios de empresas parceiras indicados para tratamento.
