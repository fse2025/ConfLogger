import os
import shutil

def copy_and_rename_dirs(removed_root_dir, original_root_dir, target_dir):
    # 确保目标目录存在
    os.makedirs(target_dir, exist_ok=True)

    # 获取两个根目录下的子目录列表
    removed_subdirs = set(os.listdir(removed_root_dir))
    original_subdirs = set(os.listdir(original_root_dir))

    # 找到相同名字的子目录
    common_subdirs = removed_subdirs & original_subdirs

    for subdir in common_subdirs:
        # 构造源路径
        removed_src = os.path.join(removed_root_dir, subdir)
        original_src = os.path.join(original_root_dir, subdir)

        # 确保源路径是目录
        if os.path.isdir(removed_src) and os.path.isdir(original_src):
            # 构造目标路径
            removed_dest = os.path.join(target_dir, f'{subdir}_removed')
            original_dest = os.path.join(target_dir, f'{subdir}_original')

            # 拷贝并重命名子目录
            shutil.copytree(removed_src, removed_dest)
            shutil.copytree(original_src, original_dest)
        else:
            print(f"Skipping {subdir} because it is not a directory.")


if __name__ == "__main__":
    removed_root_dir = "/usr/local/Lcg-src/rename-data/experiment-remove-existing-log/zookeeper"
    original_root_dir = "/usr/local/Lcg-src/rename-data/0828-zookeeper"
    target_dir = "/usr/local/Lcg-src/rename-data/original-and-replay/zookeeper"

    copy_and_rename_dirs(removed_root_dir, original_root_dir, target_dir)
