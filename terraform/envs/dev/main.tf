module "vpc" {
  source = "../../modules/vpc"
}

module "lb" {
  source = "../../modules/lb"

  public_subnet_a_id = module.vpc.public_subnet_a_id
  aws_vpc_id = module.vpc.vpc_id
  web_security_group_id = module.vpc.web_sg_id
  public_subnet_b_id = module.vpc.public_subnet_b_id
}

module "iam" {
  source = "../../modules/iam"
}

module "ecr" {
  source = "../../modules/ecr"
}

output "ecr_repository_url" {
  value = module.ecr.ecr_repository_url
}

module "ecs" {
  source = "../../modules/ecs"

  private_subnet_id = module.vpc.private_subnet_id
  ecr_repository_url = module.ecr.ecr_repository_url
  app_target_group_arn = module.lb.app_tg_arn
  web_security_group_id = module.vpc.web_sg_id
  ecs_task_execution_role_arn = module.iam.ecs_task_execution_role_arn
  http_listener_arn =  module.lb.http_listener_arn
}

module "agw" {
  source = "../../modules/agw"

  alb_dns_name      = module.lb.alb_dns_name
}

output "agw_url" {
  value = module.agw.api_gateway_url
}
