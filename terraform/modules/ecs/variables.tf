variable "private_subnet_id" {
  description = "ID of the private subnet"
  type        = string
}

variable "web_security_group_id" {
  description = "ID of the security group"
  type        = string
}

variable "app_target_group_arn" {
  description = "ARN of target group"
  type        = string
}

variable "http_listener_arn" {
  description = "HTTP listener"
  type        = string
}

variable "ecr_repository_url" {
  description = "La URL del repository ECR"
  type        = string
}

variable "ecs_task_execution_role_arn" {
  description = "ARN role in execution ECS"
  type        = string
}
