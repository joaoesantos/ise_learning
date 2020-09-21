base_dir=$1
dir_name=$2
solution_name=$3
project_name=$4

dotnet run --project $base_dir/$dir_name/$solution_name/$project_name
exit