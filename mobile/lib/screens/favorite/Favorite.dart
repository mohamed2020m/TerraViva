import 'package:TerraViva/components/ErrorDialog.dart';
import 'package:TerraViva/controller/threeDObjectController.dart';
import 'package:TerraViva/models/ThreeDObject.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';
import '../../provider/dataCenter.dart';
import 'package:TerraViva/screens/home/objects/ObjectViewScreen.dart';
import 'package:TerraViva/network/Endpoints.dart';

class Favorite extends StatefulWidget {
  const Favorite({super.key});

  @override
  State<Favorite> createState() => _FavoriteState();
}

class _FavoriteState extends State<Favorite>
    with SingleTickerProviderStateMixin {
  final ThreeDObjectController threeDObjectController =
      ThreeDObjectController();

  @override 
  void initState() {
    getCollection();
    super.initState();
  }

  Future getCollection() async {
    setState(() {});
  }


  void _navigateToObjectView(BuildContext context, ThreeDObject object) {
    var categoryProvider = Provider.of<DataCenter>(context, listen: false);
    categoryProvider.setCurretntObject3d(object);
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => ObjectViewScreen(object3d: object),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    SystemChrome.setSystemUIOverlayStyle(
      const SystemUiOverlayStyle(
        statusBarColor: Color(0xFF6D83F2),
      ),
    );

    return Scaffold(
      appBar: AppBar(
        title: Text(
          'Favorites',
          style: Theme.of(context).textTheme.headlineSmall?.copyWith(
              color: Colors.white, fontSize: 20, fontWeight: FontWeight.w900),
        ),
        centerTitle: true,
        elevation: 0,
        backgroundColor: const Color(0xFF6D83F2),
      ),
      body: RefreshIndicator(
        onRefresh: getCollection,
        child: FutureBuilder<List<ThreeDObject>>(
          future: threeDObjectController.get3DObjectsByStudent(),
          builder: (context, objectsSnapshot) {
            if (objectsSnapshot.connectionState == ConnectionState.waiting) {
              return const Center(child: CircularProgressIndicator());
            }

            if (objectsSnapshot.hasError) {
              return Center(
                child: Text(
                  'Error loading objects',
                  style: Theme.of(context).textTheme.titleMedium,
                ),
              );
            }

            final objects = objectsSnapshot.data ?? [];

            if (objects.isEmpty) {
              return ListView(
                physics: const AlwaysScrollableScrollPhysics(),
                children: [
                  SizedBox(
                    height: MediaQuery.of(context).size.height - kToolbarHeight,
                    child: Center(
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Icon(
                            Icons.image_not_supported,
                            size: 100,
                            color: Colors.grey[300],
                          ),
                          const SizedBox(height: 16),
                          Text(
                            'No objects found',
                            style: Theme.of(context).textTheme.titleMedium,
                          ),
                        ],
                      ),
                    ),
                  ),
                ],
              );
            }

            return Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16),
              child: ListView.builder(
                padding: const EdgeInsets.symmetric(vertical: 8),
                itemCount: objects.length,
                itemBuilder: (context, index) {
                  final object = objects[index];
                  return Container(
                    margin: const EdgeInsets.symmetric(vertical: 8),
                    decoration: BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.circular(12),
                      boxShadow: [
                        BoxShadow(
                          color: Colors.grey.withOpacity(0.2),
                          spreadRadius: 2,
                          blurRadius: 5,
                          offset: const Offset(0, 3),
                        ),
                      ],
                    ),
                    child: ListTile(
                      contentPadding: const EdgeInsets.all(12),
                      leading: Hero(
                        tag: 'object_image_${object.id}',
                        child: Container(
                          width: 60,
                          height: 60,
                          decoration: BoxDecoration(
                            borderRadius: BorderRadius.circular(10),
                            image: DecorationImage(
                              image: NetworkImage(
                                "${Endpoints.baseUrl}/files/download/${object.image}",
                              ),
                              fit: BoxFit.cover,
                            ),
                          ),
                        ),
                      ),
                      title: Text(
                        object.name,
                        style: Theme.of(context).textTheme.titleMedium,
                      ),
                      subtitle: Text(
                        object.description,
                        style: Theme.of(context).textTheme.bodyMedium,
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                      ),
                      trailing: const Icon(Icons.chevron_right),
                      onTap: () => _navigateToObjectView(context, object),
                    ),
                  );
                },
              ),
            );
          },
        ),
      ),
    );
  }
}
