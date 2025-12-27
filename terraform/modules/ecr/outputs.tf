output "ecr_repository_url" {
  description = "URL of the repository ECR (push/pull)"
  value       = aws_ecr_repository.ecr-franchise.repository_url
}
