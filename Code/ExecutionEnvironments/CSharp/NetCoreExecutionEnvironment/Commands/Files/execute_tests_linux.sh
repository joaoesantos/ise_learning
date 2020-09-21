base_dir=$1
dir_name=$2
solution_name=$3

fullpath=$base_dir/$dir_name/$solution_name
dotnet test $base_dir/$dir_name/$solution_name

exit