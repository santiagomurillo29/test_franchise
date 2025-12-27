output "vpc_id" {
  description = "ID of the VPC"
  value       = aws_vpc.vpc-franchise.id
}

output "public_subnet_a_id" {
  description = "ID of the public subnet a"
  value       = aws_subnet.subnet-public-a-franchise.id
}

output "public_subnet_b_id" {
  description = "ID of the public subnet b"
  value       = aws_subnet.subnet-public-b-franchise.id
}

output "private_subnet_id" {
  description = "ID of the private subnet"
  value = aws_subnet.subnet-private-a-franchise.id
}

output "web_sg_id" {
  description = "ID of security group "
  value = aws_security_group.security-group-franchise.id
}
