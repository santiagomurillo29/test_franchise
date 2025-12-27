resource "aws_apigatewayv2_api" "api_gateway_franchise" {
  name = "demo-http-api"
  protocol_type = "HTTP"
}

resource "aws_apigatewayv2_integration" "alb_integration_franchise" {
  api_id = aws_apigatewayv2_api.api_gateway_franchise.id
  integration_type = "HTTP_PROXY"
  integration_uri = "http://${var.alb_dns_name}/{proxy}"
  integration_method = "ANY"
  payload_format_version = "1.0"
}

resource "aws_apigatewayv2_route" "route_franchise" {
  api_id = aws_apigatewayv2_api.api_gateway_franchise.id
  route_key = "ANY /{proxy+}"
  target = "integrations/${aws_apigatewayv2_integration.alb_integration_franchise.id}"
}

resource "aws_apigatewayv2_stage" "default" {
  api_id = aws_apigatewayv2_api.api_gateway_franchise.id
  name = "$default"
  auto_deploy = true
}