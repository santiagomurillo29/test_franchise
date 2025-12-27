output "ecs_task_execution_role_arn" {
  description = "ARN del rol ECS Task Execution"
  value       = data.aws_iam_role.ecs_task_execution_role.arn
}
