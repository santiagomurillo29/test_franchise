variable "aws_vpc_id" {
  description = "ID of the vpc"
  type = string
}

variable "public_subnet_a_id" {
  description = "ID of the public subnet a"
  type        = string
}

variable "public_subnet_b_id" {
  description = "ID of the public subnet b"
  type        = string
}

variable "web_security_group_id" {
  description = "ID of the security group"
  type        = string
}
