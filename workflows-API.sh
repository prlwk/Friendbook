# read the workflow template
WORKFLOW_TEMPLATE=$(cat .github/workflow-microservice-template.yaml)

# iterate each route in routes directory
for ROUTE in $(ls API); do
	echo "generating workflow for API/${ROUTE}"

	# replace template route placeholder with route name
	WORKFLOW=$(echo "${WORKFLOW_TEMPLATE}" | sed "s/{{ROUTE}}/${ROUTE}/g")

	# save workflow to .github/workflows/{ROUTE}
	echo "${WORKFLOW}" > .github/workflows/${ROUTE}.yaml
done