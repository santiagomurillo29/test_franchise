output "api_gateway_url" {
  description = "API Gateway Base URL"
  value = aws_apigatewayv2_api.api_gateway_franchise.api_endpoint
}
