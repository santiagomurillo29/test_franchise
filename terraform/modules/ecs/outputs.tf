output "ecs_cluster_name" {
  description = "Name of the clúster ECS"
  value       = aws_ecs_cluster.cluster_franchise.name
}
