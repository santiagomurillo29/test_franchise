resource "aws_lb" "load-balancer-franchise" {
  name = "load-balancer-franchise"
  load_balancer_type = "application"
  internal = false
  subnets = [
    var.public_subnet_a_id,
    var.public_subnet_b_id
  ]
  security_groups = [var.web_security_group_id]

  tags = {
    Name = "load-balancer-franchise"
  }
}

resource "aws_lb_target_group" "target-group-franchise" {
  name = "target-group-franchise"
  port = 8080
  protocol = "HTTP"
  target_type = "ip"
  vpc_id = var.aws_vpc_id

  health_check {
    path = "/"
    healthy_threshold   = 3
    unhealthy_threshold = 3
    timeout             = 6
    interval            = 30
    matcher             = "200-399"
  }

  tags = {
    Name = "target-group-franchise"
  }
}

resource "aws_lb_listener" "load-balancer-listener-franchise" {
  load_balancer_arn = aws_lb.load-balancer-franchise.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.target-group-franchise.arn
  }
}