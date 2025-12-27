resource "aws_ecs_cluster" "cluster_franchise" {
  name = "cluster_franchise"

  tags = {
    Name = "dcluster_franchise"
  }
}

resource "aws_ecs_task_definition" "task_definition_franchise" {
  family                   = "task_definition_franchise"
  execution_role_arn       = var.ecs_task_execution_role_arn
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "1024"
  memory                   = "2048"

  container_definitions = jsonencode([
    {
      name      = "franchise"
      image     = "${var.ecr_repository_url}:latest"
      cpu       = 256
      memory    = 512
      essential = true
      portMappings = [
        {
          containerPort = 8080
          hostPort      = 8080
          protocol      = "tcp"
          appProtocol   = "http"
        }
      ]
      dependsOn = [
        {
          containerName = "mongo"
          condition     = "START"
        }
      ]
      environment = [
        {
          name  = "SPRING_DATA_MONGODB_URI"
          value = "mongodb://root:password@localhost:27017/test_db?authSource=admin"
        },
        {
          name  = "JAVA_OPTS"
          value = "-XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"
        }
      ]
    },
    {
      name      = "mongo"
      image     = "mongo:latest"
      cpu       = 256
      memory    = 512
      essential = true
      portMappings = [
        {
          containerPort = 27017
          hostPort      = 27017
          protocol      = "tcp"
        }
      ]
      environment = [
        {
          name  = "MONGO_INITDB_ROOT_USERNAME"
          value = "root"
        },
        {
          name  = "MONGO_INITDB_ROOT_PASSWORD"
          value = "password"
        },
        {
          name  = "MONGO_INITDB_DATABASE"
          value = "test_db"
        }
      ]
    }
  ])
}

resource "aws_ecs_service" "service_franchise" {
  name = "service_franchise"
  cluster = aws_ecs_cluster.cluster_franchise.id
  task_definition = aws_ecs_task_definition.task_definition_franchise.arn
  launch_type = "FARGATE"
  desired_count = 1

  network_configuration {
    subnets = [var.private_subnet_id]
    security_groups = [var.web_security_group_id]
    assign_public_ip = false
  }

  load_balancer {
    container_name = "franchise"
    container_port = 8080
    target_group_arn = var.app_target_group_arn
  }

  depends_on = [var.http_listener_arn]
}