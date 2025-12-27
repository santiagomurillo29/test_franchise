output "app_tg_arn" {
  description = "Franchise's target group"
  value = aws_lb_target_group.target-group-franchise.arn
}

output "http_listener" {
  description = "Franchise's load balancer listener"
  value = aws_lb_listener.load-balancer-listener-franchise
}

output "http_listener_arn" {
  description = "ARN of the load balancer listener of franchise "
  value = aws_lb_listener.load-balancer-listener-franchise.arn
}

output "alb_dns_name" {
  description = "Public DNS name of the Load Balancer"
  value       = aws_lb.load-balancer-franchise.dns_name
}
