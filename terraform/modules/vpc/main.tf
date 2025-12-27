terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "5.81.0"
    }
  }
}
resource "aws_vpc" "vpc-franchise" {
  cidr_block = "10.0.0.0/16"

  tags = {
    Name = "vpc-franchise"
  }
}


data "aws_availability_zones" "available" {
}

resource "aws_subnet" "subnet-private-a-franchise" {
  vpc_id = aws_vpc.vpc-franchise.id
  cidr_block = "10.0.2.0/24"
  availability_zone = data.aws_availability_zones.available.names[0]

  tags = {
    Name = "subnet-private-a-franchise"
  }
}

resource "aws_subnet" "subnet-public-a-franchise" {
  vpc_id            = aws_vpc.vpc-franchise.id
  cidr_block        = "10.0.1.0/24"
  availability_zone = data.aws_availability_zones.available.names[0]
  map_public_ip_on_launch = true

  tags = {
    Name = "subnet-public-a-franchise"
  }
}

resource "aws_subnet" "subnet-public-b-franchise" {
  vpc_id            = aws_vpc.vpc-franchise.id
  cidr_block        = "10.0.3.0/24"
  availability_zone = data.aws_availability_zones.available.names[1]
  map_public_ip_on_launch = true

  tags = {
    Name = "subnet-public-b-franchise"
  }
}


resource "aws_nat_gateway" "nat-franchise" {
  allocation_id = aws_eip.nat-eip.id
  subnet_id = aws_subnet.subnet-public-a-franchise.id

  tags = {
    Name = "nat-gateway-franchise"
  }

  depends_on = [aws_internet_gateway.internet-gateway-franchise]
}

resource "aws_internet_gateway" "internet-gateway-franchise" {
  vpc_id = aws_vpc.vpc-franchise.id

  tags = {
    Name = "internet-gateway-franchise"
  }
}

resource "aws_eip" "nat-eip" {
  depends_on = [aws_internet_gateway.internet-gateway-franchise]

  tags = {
    Name = "nat-eip-franchise"
  }
}

resource "aws_route_table" "route-table-public-franchise" {
  vpc_id = aws_vpc.vpc-franchise.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.internet-gateway-franchise.id
  }

  tags = {
    Name = "route-table-public-franchise"
  }
}

resource "aws_route_table" "route-table-private-franchise" {
  vpc_id = aws_vpc.vpc-franchise.id

  route {
    cidr_block = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.nat-franchise.id
  }

  tags = {
    Name = "route-table-private-franchise"
  }
}

resource "aws_route_table_association" "tabla-association-public-a-franchise" {
  subnet_id      = aws_subnet.subnet-public-a-franchise.id
  route_table_id = aws_route_table.route-table-public-franchise.id
}

resource "aws_route_table_association" "tabla-association-public-b-franchise" {
  subnet_id      = aws_subnet.subnet-public-b-franchise.id
  route_table_id = aws_route_table.route-table-public-franchise.id
}

resource "aws_route_table_association" "tabla-association-private-franchise" {
  subnet_id      = aws_subnet.subnet-private-a-franchise.id
  route_table_id = aws_route_table.route-table-private-franchise.id
}

resource "aws_security_group" "security-group-franchise" {
  name        = "security-group-franchise"
  description = "Allow inbound HTTP and HTTPS traffic"
  vpc_id      = aws_vpc.vpc-franchise.id

  ingress {
    description = "Custom TCP 8080"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "HTTP"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "HTTPS"
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "security-group-franchise"
  }
}